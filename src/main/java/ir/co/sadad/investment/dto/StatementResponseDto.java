package ir.co.sadad.investment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "تاریخچه صورتحساب مشتری")
public class StatementResponseDto {

    @Schema(title = " نوع تراکنش")
    private String transactionType;

    @Schema(title = "شناسه سند")
    private Integer statementId;

    @Schema(title = "زمان تراکنش")
    private String transactionDate;

    @Schema(title = " مقدار واریز")
    private Long debitAmount;

    @Schema(title = " مقدار برداشت")
    private Long creditAmount;

    @Schema(title = "مانده حساب")
    private Long balance;
}
