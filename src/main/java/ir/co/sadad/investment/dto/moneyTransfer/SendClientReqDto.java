package ir.co.sadad.investment.dto.moneyTransfer;

import ir.co.sadad.investment.common.enumurations.Currency;
import ir.co.sadad.investment.common.enumurations.InstructionType;
import ir.co.sadad.investment.common.enumurations.Mechanism;
import ir.co.sadad.investment.common.enumurations.ProductType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendClientReqDto {
    /**
     * شناسه درخواست انتقال
     */
    private String instructionIdentification;
    /**
     * نوع انتقال
     */
    private InstructionType instructionType;
    private Mechanism mechanism;
    private String fromAccount;
    private String targetAccount;
    private String amount;
    private Currency currency;
    /**
     * کد شرح تراکنش
     */
    private String smtCode;
    /**
     * بابت
     */
    private String purpose;
    /**
     * نوع محصول
     */
    private ProductType productType;

    /**
     * شناسه واریز
     */
    private String creditPayId;
    /**
     * شناسه برداشت
     */
    private String debitPayId;
    /**
     * توضیحات
     */
    private String descriptionInstruction;

}
