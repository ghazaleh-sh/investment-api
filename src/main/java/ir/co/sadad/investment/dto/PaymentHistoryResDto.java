package ir.co.sadad.investment.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Schema(title = "خروجی سرویس سرویس تاریخچه واریز")
@Builder
public class PaymentHistoryResDto {

    @Schema(title = "شناسه درخواست")
    private String id;
    @Schema(title = "نوع تراکنش")
    private String alias;
    @Schema(title = "حساب")
    private String accountId;
    @Schema(title = "مبلغ")
    private String amount;
    @Schema(title = "ایجاد کننده")
    private String creatorName;
    @Schema(title = "مقصد")
    private String counterpartyName;
    @Schema(title = "وضعیت واریز")
    private String status;
    @Schema(title = "زمان درخواست")
    private String initiationDate;
    @Schema(title = "شماره پیگیری")
    private String traceNo;
    @Schema(title = "شناسه واریز")
    private String creditPayId;


}
