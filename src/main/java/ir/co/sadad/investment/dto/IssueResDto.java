package ir.co.sadad.investment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ir.co.sadad.investment.common.enumurations.FundRequestType;
import lombok.Data;

/**
 * the last response that will be shown to end-user
 */
@Data
@Schema(title = "خروجی سرویس صدور")
public class IssueResDto {

    @Schema(title = "شناسه یکتا")
    private String uuid;

    @Schema(title = "شناسه سفارش")
    private String orderId;

    @Schema(title = "مبلغ صدور")
    private Long amount;

    @Schema(title = "کد ملی مشتری")
    private String nationalCode;

    @Schema(title = "نوع درخواست")
    private FundRequestType requestType;

    @Schema(title = "شناسه پرداخت")
    private String paymentId;

    @Schema(title = "زمان ثبت موفقیت آمیز صدور")
    private String createdDate;
}
