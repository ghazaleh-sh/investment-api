package ir.co.sadad.investment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@Schema(title = "درخواست صدور")
public class IssueReqDto {
    /**
     * first call, will be generated inside the issue method
     * second call, must be sent by front-end which is received through previous exception
     */
    private String uuid;

    @Schema(title = "شناسه صندوق")
    @NotBlank(message = "V_ISSUES_FUND_ID_REQUIRE")
    private String fundId;

    private String nationalCode;

    @Schema(title = "مبلغ صدور")
    @NotNull(message = "V_ISSUES_AMOUNT_REQUIRE")
    private Long amount;

    @Schema(title = "شماره حساب مبدا")
    @NotBlank(message = "V_ISSUES_ACCOUNT_REQUIRE")
    private String accountId;

}
