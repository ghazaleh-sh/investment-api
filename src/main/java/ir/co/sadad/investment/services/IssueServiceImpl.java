package ir.co.sadad.investment.services;

import ir.co.sadad.investment.common.enumurations.FundCreationState;
import ir.co.sadad.investment.common.enumurations.FundPaymentType;
import ir.co.sadad.investment.common.enumurations.JobStatus;
import ir.co.sadad.investment.common.exception.FinodadException;
import ir.co.sadad.investment.common.exception.InvestmentException;
import ir.co.sadad.investment.common.util.ConverterHelper;
import ir.co.sadad.investment.common.util.TripleDES;
import ir.co.sadad.investment.configs.FinodadErrorCodeProperties;
import ir.co.sadad.investment.dto.FundResponseDto;
import ir.co.sadad.investment.dto.IssueReqDto;
import ir.co.sadad.investment.dto.IssueResDto;
import ir.co.sadad.investment.dto.finodad.*;
import ir.co.sadad.investment.dto.moneyTransfer.SendClientResDto;
import ir.co.sadad.investment.mapper.OrderMapper;
import ir.co.sadad.investment.providers.finodad.FinodadClient;
import ir.co.sadad.investment.providers.finodad.services.BasicService;
import ir.co.sadad.investment.services.dao.InvestmentLogService;
import ir.co.sadad.investment.services.dao.InvestmentOrdersService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ir.co.sadad.investment.common.Constants.JOB_CODE;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(noRollbackFor = {RuntimeException.class})
public class IssueServiceImpl implements IssueService {

    private final FinodadClient finodadClient;
    private final InvestmentOrdersService orderService;
    private final OrderMapper orderMapper;
    private final CustomerFundService customerFundService;
    private final BasicService basicService;
    private final InvestmentLogService logService;
    private final PaymentService paymentService;
    private final TripleDES tripleDES;

    private final FinodadErrorCodeProperties finodadErrorCodesForJob;


    @Override
    @SneakyThrows
    public IssueResDto createInitialIssue(IssueReqDto issueReqDto, String customerToken, String ssn, String authorizationCode) {
        issueReqDto.setNationalCode(ssn);
        FundResponseDto fundInfoOfCustomer = customerFundService.getActiveCustomerFund(issueReqDto.getNationalCode(), issueReqDto.getFundId());

        String uuid;
        if (authorizationCode == null) {
            uuid = basicService.generateUuid(issueReqDto.getNationalCode(),
                    issueReqDto.getAmount().toString(),
                    issueReqDto.getFundId());

            issueReqDto.setUuid(uuid);
            orderService.createIssueOrder(issueReqDto);
        } else {
            uuid = issueReqDto.getUuid();
            if (uuid == null)
                throw new InvestmentException("V_UUID_ID_REQUIRE", HttpStatus.BAD_REQUEST);
        }

        SendClientResDto paymentFinalRes = paymentService.paymentSendClient(customerToken, issueReqDto,
                fundInfoOfCustomer.getBankAccountNumber(), authorizationCode);

        return createIssue(issueReqDto, paymentFinalRes.getInstructionIdentification());
    }


    public IssueResDto createIssue(IssueReqDto issueReqDto, String instructionIdentification) {
        String jsonData = null;
        String uuid = issueReqDto.getUuid();
        try {
            IssueCreationReqDto issueReq = buildIssueRequest(issueReqDto, instructionIdentification);
            FinodadBaseResponseDto<IssueCreationResDto> issueRes = finodadClient.createIssue(basicService.getToken(), issueReq);

            jsonData = ConverterHelper.convertResponseToJson(issueRes);

            if (issueRes.isSuccess()) {
                IssueResDto resDto = orderService.updateIssueOrderWithOrderId(uuid, issueRes.getData().getOrderId(), JobStatus.SUCCEEDED);
                resDto.setNationalCode(issueReqDto.getNationalCode());
                logService.saveLogs("", "",
                        uuid, jsonData, "createIssue", "SUCCESSFUL");
                return resDto;
            } else {
                logService.saveLogs("", issueRes.getErrorDetail() != null ? issueRes.getErrorDetail().getMessage() : "",
                        uuid, jsonData, "createIssue", "UNSUCCESSFUL");

                if (issueRes.getErrorDetail() != null && finodadErrorCodesForJob.getJob_error_codes().contains(issueRes.getErrorDetail().getCode())) {
                    return inquiryIssue(uuid);
                }

                orderService.updateOrderStatus(uuid, FundCreationState.UNSUCCESSFUL, JobStatus.FAILED);
                throw new FinodadException("BUSINESS_ISSUES_UNKNOWN_FAILED",
                        issueRes.getErrorDetail().getMessage(),
                        issueRes.getErrorDetail().getCode(),
                        HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            if (!(e instanceof FinodadException || e instanceof InvestmentException)) {
                logService.saveLogs(e.getClass().getName(), e.getMessage(),
                        uuid, jsonData, "createIssue", "EXCEPTION");

                return inquiryIssue(uuid);
            }
            throw e;
        }
    }

    @SneakyThrows
    @Override
    public IssueResDto inquiryIssue(String uuid) {
        String jsonData = null;
        try {
            FinodadBaseResponseDto<IssueInquiryResDto> issueInquiryRes = finodadClient.inquiryIssue(basicService.getToken(), uuid);
            jsonData = ConverterHelper.convertResponseToJson(issueInquiryRes);

            if (issueInquiryRes.isSuccess()) {
                IssueResDto resDto = orderService.updateIssueOrderWithOrderId(uuid, issueInquiryRes.getData().getOrderId(), JobStatus.SUCCEEDED);
                resDto.setNationalCode(issueInquiryRes.getData().getNationalCode());
                resDto.setPaymentId(issueInquiryRes.getData().getPaymentId());

                logService.saveLogs("", "",
                        uuid, jsonData, "inquiryIssue", "SUCCESSFUL");
                return resDto;

            } else {
                logService.saveLogs("", "",
                        uuid, jsonData, "inquiryIssue", "UNSUCCESSFUL");

                if (issueInquiryRes.getErrorDetail() != null && finodadErrorCodesForJob.getJob_error_codes().contains(issueInquiryRes.getErrorDetail().getCode())) {
                    orderService.updateOrderStatus(uuid, FundCreationState.ISSUE_BY_JOB, JobStatus.FAILED);
                } else
                    orderService.updateOrderStatus(uuid, FundCreationState.UNSUCCESSFUL, JobStatus.FAILED);

                throw new FinodadException("BUSINESS_ISSUES_INQUIRY_UNKNOWN_FAILED",
                        issueInquiryRes.getErrorDetail().getMessage(),
                        issueInquiryRes.getErrorDetail().getCode(),
                        HttpStatus.BAD_REQUEST);

            }
        } catch (Exception e) {
            if (!(e instanceof FinodadException)) {
                logService.saveLogs(e.getClass().getName(), e.getMessage(),
                        uuid, jsonData, "inquiryIssue", "EXCEPTION");
                orderService.updateOrderStatus(uuid, FundCreationState.ISSUE_BY_JOB, JobStatus.FAILED);

                throw new InvestmentException("BUSINESS_ISSUES_UNKNOWN_FAILED", HttpStatus.INTERNAL_SERVER_ERROR, JOB_CODE);
            }
            throw e;
        }
    }

    @Override
    public void cancelIssue(String uuid, String fundId, String ssn, String otp) {
        orderService.getBy(uuid);

        String jsonData = "";
        if (otp == null)
            basicService.cancelOtp(uuid, CancelOtpReqDto.builder().nationalCode(ssn).fundId(fundId).build());

        else {
            FinodadBaseResponseDto<ObjectUtils.Null> cancelIssueRes;
            try {
                cancelIssueRes = finodadClient.cancelIssue(basicService.getToken(),
                        CancelReqDto.builder()
                                .uuid(uuid)
                                .nationalCode(ssn)
                                .fundId(fundId)
                                .otp(otp).build());

                jsonData = ConverterHelper.convertResponseToJson(cancelIssueRes);
            } catch (Exception e) {
                logService.saveLogs(e.getClass().getName(), e.getMessage(),
                        uuid, jsonData, "cancelIssue", "EXCEPTION");
                throw e;//new InvestmentException("BUSINESS_CANCEL_ISSUE_FAILED", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (!cancelIssueRes.isSuccess()) {
                logService.saveLogs("", cancelIssueRes.getErrorDetail() != null ? cancelIssueRes.getErrorDetail().getMessage() : "",
                        uuid, jsonData, "cancelIssue", "UNSUCCESSFUL");

                throw new FinodadException("BUSINESS_CANCEL_ISSUE_FAILED",
                        cancelIssueRes.getErrorDetail().getMessage(),
                        cancelIssueRes.getErrorDetail().getCode(),
                        HttpStatus.BAD_REQUEST);

            } else {
                orderService.updateOrderStatus(uuid, FundCreationState.CANCELED, JobStatus.SUCCEEDED);
                logService.saveLogs("", "",
                        uuid, jsonData, "cancelIssue", "SUCCESSFUL");
            }
        }
    }

    private IssueCreationReqDto buildIssueRequest(IssueReqDto issueReqDto, String paymentId) {
        String signature = issueReqDto.getAmount() + "|" + issueReqDto.getNationalCode() + "|" + issueReqDto.getFundId() + "|" + issueReqDto.getUuid();

        IssueCreationReqDto mappedIssue = orderMapper.toIssueClient(issueReqDto);
        mappedIssue.setIssueType(FundPaymentType.ACCOUNT_TO_ACCOUNT.toString());
        mappedIssue.setPaymentId(paymentId);
        mappedIssue.setSignature(tripleDES.sign(signature));
        return mappedIssue;
    }
}
