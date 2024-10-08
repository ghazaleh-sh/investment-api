package ir.co.sadad.investment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "تاریخچه درخواست های مشتری")
public class OrderHistoryResponseDto {

    @Schema(title = "شناسه")
    private Long id;
    @Schema(title = "نام کارگزاری")
    private String fundName;
    @Schema(title = "تعداد واحد")
    private String fundUnit;
    @Schema(title = "زمان ایجاد")
    private String creationDate;
    @Schema(title = "نوع درخواست")
    private String orderType;
    @Schema(title = "مبلغ درخواست")
    private String orderAmount;
    @Schema(title = "وضعیت درخواست")
    private String status;
    @Schema(title = "مبلغ هر واحد")
    private String unitPrice;
    @Schema(title = "شناسه تولید شده درخواست در سامانه بام",
            description = "اگر این فیلد نال باشد به معنای این است که این درخواست در بام درست نشده است")
    private String uuid;


}
