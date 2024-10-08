package ir.co.sadad.investment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ir.co.sadad.investment.common.enumurations.FundCreationState;
import ir.co.sadad.investment.common.enumurations.FundRequestType;
import lombok.Data;

@Data
@Schema(title = "خروجی سرویس استعلام صدور")
public class InquiryResDto {

    @Schema(title = "شناسه یکتا")
    private String uuid;

    @Schema(title = "شناسه سفارش")
    private Integer orderId;

    @Schema(title = "کد ملی مشتری")
    private String nationalCode;

    @Schema(title = "مبلغ صدور")
    private Long amount;

    @Schema(title = "وضعیت صدور")
    private FundCreationState state;

    @Schema(title = "نوع درخواست")
    private FundRequestType requestType;

    @Schema(title = "شناسه پرداخت")
    private String paymentId;

    @Schema(title = "اطلاعات بیشتر - توضیحات")
    private String additionalData;

    @Schema(title = "زمان ثبت موفقیت آمیز صدور")
    private String createdAt;

    @Schema(title = "زمان آخرین تغییر وضعیت صدور")
    private String updatedAt;
}
