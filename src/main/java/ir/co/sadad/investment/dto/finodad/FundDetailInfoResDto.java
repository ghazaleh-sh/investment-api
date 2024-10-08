package ir.co.sadad.investment.dto.finodad;

import lombok.Data;

import java.io.Serializable;

@Data
public class FundDetailInfoResDto implements Serializable {
    /**
     * نام صندوق سرمایه گذاری
     */
    private String name;
    /**
     * تعداد مشتریان فعال
     */
    private String activeCustomer;
    /**
     * تاریخ محاسبه آخرین NAV به شمسی (YYYY/MM/DD)
     */
    private String calcDate;
    /**
     * قیمت ریالی هر واحد برای صدور
     */
    private String purchaseNav;
    /**
     * مقدار ریالی هر واحد برای ابطال
     */
    private String saleNav;
    /**
     * بازده ماهانه صندوق
     */
    private String monthlyReturn;
    /**
     * بازده ۳ ماهه صندوق
     */
    private String threeMonthReturn;
    /**
     * بازده ۶ ماهه صندوق
     */
    private String sixMonthReturn;
    /**
     * بازده سالانه صندوق
     */
    private String yearlyReturn;
    /**
     * بازده صندوق از ابتدا
     */
    private String allDaysReturn;
    /**
     * تعداد کل واحدهای فعال در صندوق
     */
    private String fundCapital;


}
