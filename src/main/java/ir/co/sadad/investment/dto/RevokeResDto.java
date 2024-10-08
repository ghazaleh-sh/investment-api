package ir.co.sadad.investment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ir.co.sadad.investment.common.enumurations.FundRequestType;
import lombok.Data;

@Data
@Schema(title = "خروجی سرویس ابطال")
public class RevokeResDto {
    @Schema(title = "شناسه یکتا")
    private String uuid;

    @Schema(title = "شناسه سفارش")
    private String orderId;

    @Schema(title = "تعداد واحد ابطال")
    private Long fundUnit;

    @Schema(title = "کد ملی مشتری")
    private String nationalCode;

    @Schema(title = "نوع درخواست")
    private FundRequestType requestType;

    @Schema(title = "زمان ثبت موفقیت آمیز ابطال")
    private String createdDate;
}
