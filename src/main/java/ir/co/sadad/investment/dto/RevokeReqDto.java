package ir.co.sadad.investment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(title = "درخواست ابطال")
public class RevokeReqDto {

    @Schema(title = "شناسه صندوق")
    @NotBlank(message = "V_ISSUES_FUND_ID_REQUIRE")
    private String fundId;

    @Schema(title = "تغداد واحد ابطال")
    @NotBlank(message = "V_REVOKE_FUND_UNIT_REQUIRE")
    private String fundUnit;

    private String uuid;
    private String nationalCode;
}
