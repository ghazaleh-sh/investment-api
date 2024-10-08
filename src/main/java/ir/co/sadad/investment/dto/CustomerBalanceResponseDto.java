package ir.co.sadad.investment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "خروجی دارایی های مشتری")
public class CustomerBalanceResponseDto {

    @Schema(title = "دارایی مشتری بر اساس تعداد واحد صندوق")
    private Integer fundUnit;

    @Schema(title = "معادل ریالی دارایی مشتری")
    private Long asset;


}
