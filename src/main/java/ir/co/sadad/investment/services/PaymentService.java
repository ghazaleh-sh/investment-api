package ir.co.sadad.investment.services;

import ir.co.sadad.investment.common.enumurations.SortDirection;
import ir.co.sadad.investment.dto.IssueReqDto;
import ir.co.sadad.investment.dto.moneyTransfer.PaymentInquiryResDto;
import ir.co.sadad.investment.dto.PaymentHistoryResDto;
import ir.co.sadad.investment.dto.PaymentHistorySearchFilterDto;
import ir.co.sadad.investment.dto.moneyTransfer.SendClientResDto;

import java.util.List;

public interface PaymentService {

    SendClientResDto paymentSendClient(String customerToken, IssueReqDto issueReqDto, String targetAccount, String authorizationCode);

    PaymentInquiryResDto paymentInquiry(String instructionIdentification);
    /**
     * get payment history of user based on FundID
     * @param pageNumber page number
     * @param pageSize page size
     * @param sortType sort direction
     * @param searchFilter filter for search - contains FundId
     * @return list of saved payment
     */
    List<PaymentHistoryResDto> getPaymentHistory(Integer pageNumber,
                                                 Integer pageSize,
                                                 SortDirection sortType,
                                                 PaymentHistorySearchFilterDto searchFilter);
}
