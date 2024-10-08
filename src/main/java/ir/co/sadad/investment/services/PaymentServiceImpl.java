package ir.co.sadad.investment.services;


import ir.co.sadad.investment.common.Empty;
import ir.co.sadad.investment.common.enumurations.*;
import ir.co.sadad.investment.common.exception.InvestmentException;
import ir.co.sadad.investment.common.exception.MoneyTransferException;
import ir.co.sadad.investment.common.exception.PreconditionFailedException;
import ir.co.sadad.investment.common.util.ConverterHelper;
import ir.co.sadad.investment.dto.IssueReqDto;
import ir.co.sadad.investment.dto.PaymentHistoryResDto;
import ir.co.sadad.investment.dto.PaymentHistorySearchFilterDto;
import ir.co.sadad.investment.dto.eb.EbAccountResponseDto;
import ir.co.sadad.investment.dto.moneyTransfer.OtpResDto;
import ir.co.sadad.investment.dto.moneyTransfer.PaymentInquiryResDto;
import ir.co.sadad.investment.dto.moneyTransfer.SendClientReqDto;
import ir.co.sadad.investment.dto.moneyTransfer.SendClientResDto;
import ir.co.sadad.investment.entities.InvestmentOrder;
import ir.co.sadad.investment.entities.InvestmentPayment;
import ir.co.sadad.investment.providers.eb.EBClient;
import ir.co.sadad.investment.providers.moneyTransfer.MoneyTransferClient;
import ir.co.sadad.investment.providers.sso.services.SSOTokenService;
import ir.co.sadad.investment.repositories.InvestmentPaymentSpec;
import ir.co.sadad.investment.services.dao.InvestmentLogService;
import ir.co.sadad.investment.services.dao.InvestmentOrdersService;
import ir.co.sadad.investment.services.dao.InvestmentPaymentService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ir.co.sadad.investment.common.Constants.JOB_CODE;

@Service
@Slf4j
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final MoneyTransferClient moneyTransferClient;
    private final InvestmentPaymentService paymentService;
    private final InvestmentOrdersService orderService;
    private final EBClient ebClient;
    private final InvestmentLogService logService;
    private final SSOTokenService ssoTokenService;

    private final InvestmentJobService asyncTaskService;

//    private final String tempClientCredToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJncmFudCI6IkNMSUVOVCIsImlzcyI6Imh0dHA6Ly9hcGkuYm1pLmlyL3NlY3VyaXR5IiwiYXVkIjoidHJhdmVsLWN1cnJlbmN5LWNsaWVudCIsImV4cCI6MTcyMjgzNDgyMzgzOCwibmJmIjoxNzIyNzQ4NDIzODM4LCJyb2xlIjoiIiwic2VyaWFsIjoiYTRlODQ0MWMtODg1MS0zYzAwLWE3ZjUtNzIxN2NkNDI4NjdhIiwic3NuIjoiMTU0MiIsImNsaWVudF9pZCI6IjE1NDIiLCJzY29wZXMiOlsic3ZjLW1nbXQtc2FuYS10cnYtY3Vyci1wdXItYXV0aCIsInN2Yy1tZ210LXNhbmEtdHJ2LWN1cnItc2FsZS1pbmZvLXNlbmQiLCJzdmMtbWdtdC1zYW5hLXRydi1yZWNwdC1pc3N1ZSIsInN2Yy1tZ210LWV4Y2gtc2FsZS1pbmZvLWlucSIsInNzby1tYW5hZ2VyLXNwZWNpYWwtdGFuIiwic2VuZC1tZXNzYWdlIiwibW9uZXktdHJhbnNmZXIiLCJzdmMtbWdtdC1zYW5hLXRydi1jdXJyLXNhbGUtdHJ4LWluZm8iLCJzdmMtbWdtdC1zYW5hLXRydi1jdXJyLWNhbiIsInN2Yy1tZ210LWV4Y2gtc2FsZS1pbmZvLWNmbSJdfQ==.e9-reg4K_UBKwHVknYYlW-R-hGcQrgyjyu0RNyG2BCU";

    @Override
    @SneakyThrows
    public SendClientResDto paymentSendClient(String customerToken, IssueReqDto issueReqDto, String targetAccount, String authorizationCode) {

        SendClientReqDto paymentReq = buildPaymentSendClientReq(issueReqDto.getUuid(), issueReqDto.getAccountId(), targetAccount,
                issueReqDto.getAmount().toString(), issueReqDto.getNationalCode());

        checkOwnerAccount(issueReqDto.getNationalCode(), issueReqDto.getAccountId());
        SendClientResDto moneyTransferRes = null;
        String jsonData = "";
        try {
            if (authorizationCode == null) {
                moneyTransferRes = moneyTransferClient.clientSend(ssoTokenService.getToken(), paymentReq);

            } else {
                moneyTransferRes = moneyTransferClient.clientSendWithCode(ssoTokenService.getToken(), paymentReq, authorizationCode);

                InvestmentPayment updatedPayment = paymentService.updatePaymentSuccess(paymentReq.getInstructionIdentification(), moneyTransferRes);
                orderService.updateIssueOrderPaymentStatus(updatedPayment, FundCreationState.PAYMENT_SUCCESSFUL, JobStatus.SUCCEEDED);
            }

            jsonData = ConverterHelper.convertResponseToJson(moneyTransferRes);
            logService.saveLogs("", "",
                    issueReqDto.getUuid(), jsonData, "paymentSendClient", "PAYMENT_SUCCESSFUL");

            return moneyTransferRes;

        } catch (Exception e) {
//            jsonData = ConverterHelper.convertResponseToJson(moneyTransferRes);
            if (e instanceof PreconditionFailedException) {
                InvestmentPayment savedPayment = paymentService.createPaymentRequest(paymentReq);
                orderService.updateIssueOrderWithPaymentEntity(issueReqDto.getUuid(), savedPayment);

                logService.saveLogs(e.getClass().getName(), e.getMessage(),
                        issueReqDto.getUuid(), "", "paymentSendClient", "PAYMENT_TAN");

                throw new PreconditionFailedException(OtpResDto.builder().uuid(issueReqDto.getUuid()).build());

            } else {


                // if calling clientSend for getting authorization code failed, throw exception and finish the flow
                if (authorizationCode == null) {
                    orderService.updateOrderStatus(issueReqDto.getUuid(), FundCreationState.PAYMENT_UNSUCCESSFUL, JobStatus.FAILED);

                    // for all types of 4XX exception except "TRANSFER_UNKNOWN" message, throw exception and finish the flow
                } else if (e instanceof MoneyTransferException &&
                        ((MoneyTransferException) e).getHttpStatus().equals(HttpStatusCode.valueOf(400)) &&
                        !e.getMessage().equals("TRANSFER_UNKNOWN")) {

                    logService.saveLogs(e.getClass().getName(), e.getMessage(),
                            issueReqDto.getUuid(),((MoneyTransferException) e).getJsonError() , "paymentSendClient", "PAYMENT_UNSUCCESSFUL");

                    InvestmentPayment updatedPayment = paymentService.updatePaymentStatus(paymentReq.getInstructionIdentification(), TransactionStatus.FAILED);
                    orderService.updateIssueOrderPaymentStatus(updatedPayment, FundCreationState.PAYMENT_UNSUCCESSFUL, JobStatus.FAILED);

                }

                // if other exception types like connectionException(500)
                // and MoneyTransferException with "TRANSFER_UNKNOWN" message happened, tries paymentInquiry after 10 sec
                else {
                    InvestmentPayment updatedPayment = paymentService.updatePaymentStatus(paymentReq.getInstructionIdentification(), TransactionStatus.UNKNOWN_PAYMENT);
                    orderService.updateIssueOrderPaymentStatus(updatedPayment, FundCreationState.PAYMENT_BY_JOB, JobStatus.FAILED);

                    // Scheduled asynchronous task to run in 10 seconds
                    asyncTaskService.executePaymentInquiryTask(issueReqDto, updatedPayment.getInstructionIdentification(), 10);
                    logService.saveLogs(e.getClass().getName(), e.getMessage(),
                            issueReqDto.getUuid(),e instanceof MoneyTransferException ? ((MoneyTransferException ) e).getJsonError() : null, "paymentSendClient", "PAYMENT_UNSUCCESSFUL");
                    throw new InvestmentException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, JOB_CODE);
                }
            }
            throw e;
        }
    }

    //called by job and scheduled executor
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = {RuntimeException.class})
    public PaymentInquiryResDto paymentInquiry(String instructionIdentification) {
        String jsonData = "";
        try {
            PaymentInquiryResDto moneyTransferInquiryRes = moneyTransferClient.paymentInquiry(ssoTokenService.getToken(),
                    instructionIdentification);

            InvestmentPayment updatedPayment = paymentService.updatePaymentSuccess(instructionIdentification, moneyTransferInquiryRes);
            orderService.updateIssueOrderPaymentStatus(updatedPayment, FundCreationState.ISSUE_BY_JOB, JobStatus.PROCESSING);

            jsonData = ConverterHelper.convertResponseToJson(moneyTransferInquiryRes);
            logService.saveLogs("", "",
                    instructionIdentification, jsonData, "paymentInquiry", "PAYMENT_SUCCESSFUL");

            return moneyTransferInquiryRes;
        } catch (Exception e) {

            if (e instanceof MoneyTransferException &&
                    ((MoneyTransferException) e).getHttpStatus().equals(HttpStatusCode.valueOf(400)) &&
                    !e.getMessage().equals("TRANSFER_UNKNOWN")) {
                InvestmentPayment updatedPayment = paymentService.updatePaymentStatus(instructionIdentification, TransactionStatus.FAILED);
                orderService.updateIssueOrderPaymentStatus(updatedPayment, FundCreationState.PAYMENT_UNSUCCESSFUL, JobStatus.FAILED);
                logService.saveLogs(e.getClass().getName(), e.getMessage(),
                        instructionIdentification, ((MoneyTransferException) e).getJsonError(), "paymentInquiry", "PAYMENT_UNSUCCESSFUL");
            } else {
                InvestmentPayment updatedPayment = paymentService.updatePaymentStatus(instructionIdentification, TransactionStatus.UNKNOWN_PAYMENT);
                orderService.updateIssueOrderPaymentStatus(updatedPayment, FundCreationState.PAYMENT_BY_JOB, JobStatus.FAILED);

                logService.saveLogs(e.getClass().getName(), e.getMessage(),
                        instructionIdentification, e instanceof MoneyTransferException ? ((MoneyTransferException ) e).getJsonError() : null, "paymentInquiry", "PAYMENT_UNSUCCESSFUL");

                throw new InvestmentException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, JOB_CODE);

            }
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentHistoryResDto> getPaymentHistory(Integer pageNumber,
                                                        Integer pageSize,
                                                        SortDirection sortType,
                                                        PaymentHistorySearchFilterDto searchFilter) {
        List<PaymentHistoryResDto> response = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, (sortType.equals(SortDirection.ASC)) ?
                Sort.by("payment.initiationDate").ascending() : Sort.by("payment.initiationDate").descending());

        try {

            Page<InvestmentOrder> paginationList = orderService.getPaginationList(Specification
                    .where(InvestmentPaymentSpec.filterBy(searchFilter)), pageable);
            paginationList.forEach(request -> {
                response.add(
                        PaymentHistoryResDto.builder()
                                .id(request.getPayment().getId().toString())
                                .accountId(request.getPayment().getFromAccount())
                                .amount(request.getPayment().getAmount().toString())
                                .status(Empty.isEmpty(request.getPayment().getTransactionStatus()) ? TransactionStatus.UNKNOWN_PAYMENT.name() :
                                        request.getPayment().getTransactionStatus().name())
                                .initiationDate(request.getPayment().getInitiationDate())
                                .traceNo(request.getPayment().getTraceId())
                                .creditPayId(request.getPayment().getCreditPayId())
                                .build()
                );
            });

            return response;

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }


    private SendClientReqDto buildPaymentSendClientReq(String uuid, String fromAccount, String targetAccount, String amount, String ssn) {

        return SendClientReqDto.builder()
                .instructionIdentification(uuid)
                .instructionType(InstructionType.SINGLE)
                .mechanism(Mechanism.INTRA_BANK)
                .fromAccount(fromAccount)
                .targetAccount(targetAccount)
                .amount(amount)
                .currency(Currency.IRR)
                .productType(ProductType.INDIVIDUAL)
                .creditPayId(ssn)
                .build();
    }

    /**
     * check that selected account belongs to user or not
     * <pre>
     *     this method will use EB service
     * </pre>
     *
     * @param ownerSSN ssn of user
     * @param account  selected user
     * @throws InvestmentException when account does not belong to user
     */
    private void checkOwnerAccount(String ownerSSN, String account) {
        try {
            List<EbAccountResponseDto> ownerAccounts = ebClient.getOwnerAccounts(ownerSSN);
            if (ownerAccounts.stream().noneMatch(item -> item.getId().equals(account))) {
                throw new InvestmentException("BUSINESS_ACCOUNT_DOES_NOT_BELONG_TO_USER", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            if (e instanceof InvestmentException)
                throw e;
            log.error("ERROR IN EB SERVICE =====> {0}", e);
            logService.saveLogs(e.getClass().getName(),
                    e.getMessage(),
                    null,
                    "THERE IS NO DATA JUST SSN => " + ownerSSN + "AND ACCOUNT => " + account,
                    "checkOwnerAccount",
                    "EXCEPTION");
            throw new InvestmentException("GENERAL_UNKNOWN_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
