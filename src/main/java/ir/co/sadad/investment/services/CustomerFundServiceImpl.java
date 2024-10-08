package ir.co.sadad.investment.services;

import ir.co.sadad.investment.common.Empty;
import ir.co.sadad.investment.common.exception.FinodadException;
import ir.co.sadad.investment.common.exception.InvestmentException;
import ir.co.sadad.investment.common.exception.PreconditionFailedException;
import ir.co.sadad.investment.dto.*;
import ir.co.sadad.investment.dto.finodad.*;
import ir.co.sadad.investment.entities.InvestmentFundUser;
import ir.co.sadad.investment.mapper.CustomerFundMapper;
import ir.co.sadad.investment.providers.finodad.FinodadClient;
import ir.co.sadad.investment.providers.finodad.services.BasicService;
import ir.co.sadad.investment.services.dao.InvestmentFundUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static ir.co.sadad.investment.common.Constants.FUND_DOC_POST_FIX;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerFundServiceImpl implements CustomerFundService {
    private final FinodadClient finodadClient;
    private final BasicService finodadToken;
    private final CustomerFundMapper mapper;
    private final InvestmentFundUserService investmentFundUserService;

    @Override
    @Transactional(readOnly = true) //DB does not need to be locked, concurrency improved
    public List<FundResponseDto> getCustomerFund(String customerSSN, String baseUrl) {
        List<InvestmentFundUser> userFunds = investmentFundUserService.getBy(customerSSN);
        List<FundResponseDto> finodadFunds = getFinodadFunds();
        for (FundResponseDto fund : finodadFunds) {
            fund.setActive(userFunds != null &&
                    !userFunds.isEmpty() &&
                    userFunds.stream().anyMatch(userFund -> Objects.equals(userFund.getFundId(), fund.getId().toString())));

            fund.setFundDocDownloadLink(Empty.isEmpty(baseUrl) ? "" : baseUrl + fund.getId().toString() + FUND_DOC_POST_FIX);
        }
        return finodadFunds;

    }

    @Override
    public FundInfoResponseDto getFundInfo(Integer id) {
        FinodadBaseResponseDto<FundDetailInfoResDto> response =
                finodadClient.fundInfo(finodadToken.getToken(), id);
        if (response.isSuccess()) {
            return mapper.toFundInfoResponse(response.getData());
        } else {
            throw new FinodadException("BUSINESS_FUND_INFO_UNKNOWN_FAILED",
                    response.getErrorDetail().getMessage(),
                    response.getErrorDetail().getCode(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public CustomerInfoResponseDto getCustomerInfo(String customerSSN, Integer fundId) {
        FinodadBaseResponseDto<CustomerInfoResDto> response = finodadClient.customerInfo(finodadToken.getToken(), fundId, customerSSN);
        if (response.isSuccess()) {
            return mapper.toCustomerInfoResponse(response.getData());
        } else
            throw new FinodadException("BUSINESS_CUSTOMER_INFO_UNKNOWN_FAILED",
                    response.getErrorDetail().getMessage(),
                    response.getErrorDetail().getCode(),
                    HttpStatus.BAD_REQUEST);
    }


    @Override
    public CustomerBalanceResponseDto getCustomerBalance(String customerSSN, Integer fundId) {
        FinodadBaseResponseDto<CustomerBalanceResDto> response = finodadClient.customerBalance(finodadToken.getToken(), fundId, customerSSN);
        if (response.isSuccess()) {
            return mapper.toCustomerBalanceResponse(response.getData());
        } else
            throw new FinodadException("BUSINESS_CUSTOMER_BALANCE_UNKNOWN_FAILED",
                    response.getErrorDetail().getMessage(),
                    response.getErrorDetail().getCode(),
                    HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional //all operations(like checking the existence of a customer fund and creating a new one)
    // are executed within the same transaction, providing consistency and atomicity.
    public CustomerInquiryResponseDto getCustomerInquiry(String customerSSN, Integer fundId) {
        FinodadBaseResponseDto<CustomerInquiryResDto> response =
                finodadClient.customerInquiry(finodadToken.getToken(), fundId, customerSSN);
        if (response.isSuccess()) {
            if (response.getData().getFund()) {
                FundBasicReqDto fundUser = FundBasicReqDto.builder()
                        .fundId(fundId.toString())
                        .nationalCode(customerSSN)
                        .build();
                if (!investmentFundUserService.existsCustomerFund(fundUser))
                    investmentFundUserService.createFundUser(fundUser);
            }

            return mapper.toCustomerInquiryResponse(response.getData());
        } else
            throw new FinodadException("BUSINESS_CUSTOMER_INQUIRY_UNKNOWN_FAILED",
                    response.getErrorDetail().getMessage(),
                    response.getErrorDetail().getCode(),
                    HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional
    public void register(String customerSSN, Integer fundId, String otp) {
        if (Empty.isEmpty(otp)) {
            FundBasicReqDto requestBody = FundBasicReqDto.builder()
                    .fundId(fundId.toString())
                    .nationalCode(customerSSN)
                    .build();
            FinodadBaseResponseDto<ObjectUtils.Null> sejamResponse = finodadClient.sejamOtp(finodadToken.getToken(), requestBody);
            if (sejamResponse.isSuccess()) {
                throw new PreconditionFailedException();
            } else

                throw new FinodadException("BUSINESS_SEJAM_OTP_UNKNOWN_FAILED",
                        sejamResponse.getErrorDetail().getMessage(),
                        sejamResponse.getErrorDetail().getCode(),
                        HttpStatus.BAD_REQUEST);
        } else {

            CustomerRegisterReqDto req =
                    CustomerRegisterReqDto.builder()
                            .otp(otp)
                            .fundId(fundId.toString())
                            .nationalCode(customerSSN)
                            .build();
            FinodadBaseResponseDto<ObjectUtils.Null> registerRes = finodadClient.register(finodadToken.getToken(), req);
            if (registerRes.isSuccess()) {
                FundBasicReqDto fundUser = FundBasicReqDto.builder()
                        .fundId(fundId.toString())
                        .nationalCode(customerSSN)
                        .build();
                if (!investmentFundUserService.existsCustomerFund(fundUser))
                    investmentFundUserService.createFundUser(fundUser);
            } else

                throw new FinodadException("BUSINESS_REGISTER_UNKNOWN_FAILED",
                        registerRes.getErrorDetail().getMessage(),
                        registerRes.getErrorDetail().getCode(),
                        HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<OrderHistoryResponseDto> getOrderHistory(String customerSSN, Integer fundId, Integer page, Integer size) {
        FinodadBaseResponseDto<CustomerOrderHistoryResDto> orderHistoryRes = finodadClient.orderHistory(finodadToken.getToken(),
                fundId,
                customerSSN,
                Math.max(page - 1, 0),
                size);
        if (orderHistoryRes.isSuccess()) {
            return mapper.toOrderHistoryResponseList(orderHistoryRes.getData().getOrders());
        } else

            throw new FinodadException("BUSINESS_ORDER_HISTORY_UNKNOWN_FAILED",
                    orderHistoryRes.getErrorDetail().getMessage(),
                    orderHistoryRes.getErrorDetail().getCode(),
                    HttpStatus.BAD_REQUEST);
    }

    @Override
    public List<StatementResponseDto> getCustomerStatement(String customerSSN, Integer fundId) {

        FinodadBaseResponseDto<CustomerStatementResDto> statement = finodadClient.statement(finodadToken.getToken(), fundId, customerSSN);

        if (statement.isSuccess()) {
            if (statement.getData().getStatements()== null ||  statement.getData().getStatements().isEmpty())
                return Collections.emptyList();
            return mapper.toCustomerStatementResponseList(statement.getData().getStatements());
        } else {
            throw new FinodadException("BUSINESS_STATEMENT_UNKNOWN_FAILED",
                    statement.getErrorDetail().getMessage(),
                    statement.getErrorDetail().getCode(),
                    HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public List<FundResponseDto> getFinodadFunds() {
        FinodadBaseResponseDto<FundListResponseDto> funds =
                finodadClient.fundList(finodadToken.getToken());
        if (funds.isSuccess()) {
            return mapper.toFundListResponseList(funds.getData().getFunds());
        } else {
            throw new FinodadException("BUSINESS_FUNDS_UNKNOWN_FAILED",
                    funds.getErrorDetail().getMessage(),
                    funds.getErrorDetail().getCode(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public FundResponseDto getActiveCustomerFund(String nationalCode, String fundId) {

        Optional<FundResponseDto> activeFundInfoForCustomer = getCustomerFund(nationalCode, "")
                .stream()
                .filter(fundRes -> fundRes.getId().toString().equals(fundId) && fundRes.isActive())
                .findFirst();

        if (activeFundInfoForCustomer.isEmpty())
            throw new InvestmentException("BUSINESS_ACTIVE_FUND_NOT_FOUND", HttpStatus.NOT_FOUND);

        return activeFundInfoForCustomer.get();
    }

}
