package ir.co.sadad.investment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "خروجی استعلام مشتری")
public class CustomerInquiryResponseDto {

    @Schema(title = "اگر کدملی سجامی باشد true  ودر غیر این صورت false می باشد")
    private boolean sejam;
    @Schema(title = "اگر مشتری صندوق باشد true  ودر غیر این صورت false می باشد")
    private boolean fund;

}
