package ir.co.sadad.investment.dto.moneyTransfer;

import lombok.Data;

@Data
public class SendClientResDto extends PaymentInquiryResDto {

    /**
     * شرح دستورالعمل
     */
    private String instructionDescription;

    /**
     * شناسه تراکنش
     */
    private String transactionId;
    /**
     * شناسه واریز
     */
    private String creditPayId;
    /**
     * شناسه برداشت
     */
    private String debitPayId;

    /**
     * نام صاحب حساب مقصد
     */
    private String payeeName;
}
