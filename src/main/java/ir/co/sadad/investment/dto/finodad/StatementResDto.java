package ir.co.sadad.investment.dto.finodad;

import lombok.Data;

@Data
public class StatementResDto {
    /**
     * نوع تراکنش
     */
    private String transactionType;

    /**
     * شناسه سند
     */
    private Integer statementId;

    /**
     *زمان ارسال درخواست شمسی
     */
    private String transactionDate;


    private String transactionDateTimestamp;

    /**
     * مقدار واریز
     */
    private Long debitAmount;

    /**
     * مقدار برداشت
     */
    private Long creditAmount;

    /**
     * مانده حساب
     */
    private Long balance;


}
