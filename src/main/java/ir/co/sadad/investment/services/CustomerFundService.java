package ir.co.sadad.investment.services;

import ir.co.sadad.investment.dto.*;

import java.util.List;

/**
 * service for manage customer and fund
 */
public interface CustomerFundService {

    /**
     * get list of funds
     * <pre>
     *     this service will get list of fund from Finodad service and merge with user funds .
     *     there is a field for that : isActive
     * </pre>
     *
     * @param customerSSN ssn of user - for getting customer funds
     * @param baseUrl     base url of client - fetch from header - use for create download link of fund document
     * @return list of funds
     */
    List<FundResponseDto> getCustomerFund(String customerSSN, String baseUrl);

    /**
     * get fund list from Finodad services
     *
     * @return list of funds
     */
    List<FundResponseDto> getFinodadFunds();

    /**
     * get active customer fund
     *
     * @param nationalCode ssn of customer
     * @param fundId       id of fund
     * @return active customer fund
     * @throws ir.co.sadad.investment.common.exception.InvestmentException when not found active fund for customer
     */
    FundResponseDto getActiveCustomerFund(String nationalCode, String fundId);

    /**
     * get info of a fund
     *
     * @param id id of a fund
     * @return fund info
     */
    FundInfoResponseDto getFundInfo(Integer id);

    /**
     * get customer info based on fundId and ssn of customer
     *
     * @param customerSSN ssn of customer
     * @param fundId      id of fund
     * @return info of customer
     */
    CustomerInfoResponseDto getCustomerInfo(String customerSSN, Integer fundId);

    /**
     * get balance of user based on fund id
     *
     * @param fundId      fund id
     * @param customerSSN ssn of customer
     * @return balance of customer
     */
    CustomerBalanceResponseDto getCustomerBalance(String customerSSN, Integer fundId);

    /**
     * service for inquiry customer on a fund
     * <pre>
     *     if fund was true in response of Finodad service and there is no data in DB , must save
     *     in CustomerFund DB .
     * </pre>
     *
     * @param customerSSN ssn of customer
     * @param fundId      id of fund
     * @return inquiry of customer
     */
    CustomerInquiryResponseDto getCustomerInquiry(String customerSSN, Integer fundId);

    /**
     * register customer
     * <pre>
     *     if there was no OTP , then must call SejamOtp from Finodad
     *     and throw 412 ,
     *     if request has OTP then must call Register service and store fund in DB
     * </pre>
     *
     * @param customerSSN ssn of user
     * @param fundId      id of fund
     * @param otp         otp code - optional
     * @throws ir.co.sadad.investment.common.exception.PreconditionFailedException when successfully call Sejam service
     */
    void register(String customerSSN, Integer fundId, String otp);

    /**
     * get order history of user
     *
     * @param customerSSN ssn of user
     * @param fundId      id of fund
     * @param page        page number - start from 0
     * @param size        size of page
     * @return list of customer order history - from Finodad services
     */
    List<OrderHistoryResponseDto> getOrderHistory(String customerSSN,
                                                  Integer fundId,
                                                  Integer page,
                                                  Integer size);

    /**
     * get statement of customer
     *
     * @param customerSSN ssn of customer
     * @param fundId      id of fund
     * @return list of statements - From Finodad services
     */
    List<StatementResponseDto> getCustomerStatement(String customerSSN,
                                                    Integer fundId);
}
