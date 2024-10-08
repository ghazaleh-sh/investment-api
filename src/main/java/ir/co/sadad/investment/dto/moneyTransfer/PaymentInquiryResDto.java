package ir.co.sadad.investment.dto.moneyTransfer;

import ir.co.sadad.investment.common.enumurations.InstructionType;
import ir.co.sadad.investment.common.enumurations.Mechanism;
import ir.co.sadad.investment.common.enumurations.TransactionStatus;
import ir.co.sadad.investment.dto.finodad.ThirdPartyServicesResponse;
import lombok.Data;

@Data
public class PaymentInquiryResDto implements ThirdPartyServicesResponse {
    /**
     * شناسه درخواست انتقال
     */
    private String instructionIdentification;
    /**
     * تاریخ و رمان درخواست
     */
    private String initiationDate;
    /**
     * تاریخ و زمان اجرای تراکنش
     */
    private String executionDate;
    private String currency;
    /**
     * نوع دستورالعمل
     */
    private InstructionType instructionType;
    private String fromAccount;
    private String targetAccount;
    private String amount;
    private Mechanism mechanism;
    /**
     * وضعیت تراکنش
     */
    private TransactionStatus transactionStatus;
    /**
     * توضیح تراکنش
     */
    private String transactionDescription;
    /**
     * شماره پیگیری تراکنش
     */
    private String traceId;
    /**
     * شماره پیگیری (داخلی)
     */
    private String statementTraceId;

}
