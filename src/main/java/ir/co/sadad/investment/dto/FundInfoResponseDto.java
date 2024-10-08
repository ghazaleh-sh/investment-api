package ir.co.sadad.investment.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "مشخصات صندوق")
@Data
public class FundInfoResponseDto {

    @Schema(title = "مشتری قعال")
    private String activeCustomer;

    @Schema(title = "تاریخ آخرین محاسبه")
    private String calcDate;

    @Schema(title = "قیمت ریالی برای هر واحد سدور")
    private String purchaseNav;

    @Schema(title = "مقدار ریالی هر واحد برای ابطال")
    private String saleNav;

    @Schema(title = "بازده ماهانه صندوق")
    private String monthlyReturn;

    @Schema(title = "بازده ۳ ماهه صندوق")
    private String threeMonthReturn;

    @Schema(title = "بازده ۶ ماهه صندوق")
    private String sixMonthReturn;

    @Schema(title = "بازده سالانه صندوق")
    private String yearlyReturn;

    @Schema(title = "بازده صندوق از ابتدا")
    private String allDaysReturn;

    @Schema(title = "تعداد کل واحدهای فعال در صندوق")
    private String fundCapital;

}
