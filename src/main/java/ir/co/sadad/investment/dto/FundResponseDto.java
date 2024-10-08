package ir.co.sadad.investment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "خروجی لیست صندوق ها", description = "خروجی سرویس لیست صندوق ها")
public class FundResponseDto {

    @Schema(title = "شناسه صندوق")
    private Integer id;

    @Schema(title = "نام صندوق")
    private String name;

    @Schema(title = "شعبه صندوق")
    private String bankBranch;

    @Schema(title = "بانک  صندوق")
    private String bankName;

    @Schema(title = "شماره حساب صندوق")
    private String bankAccountNumber;

    @Schema(title = "آیا صندوق فعال است")
    private boolean active;

    @Schema(title = "لینک دانلود اسناد صندوق", description = "این لینک بر اساس کلاینت صدا کننده متفاوت است و باید بیس کلاینت به آن اضافه گردد")
    private String fundDocDownloadLink;

}
