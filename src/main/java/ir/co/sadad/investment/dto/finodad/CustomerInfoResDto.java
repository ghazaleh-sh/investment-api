package ir.co.sadad.investment.dto.finodad;

import lombok.Data;

import java.io.Serializable;

/**
 * پاسخ سرویس دریافت اطلاعات مشتری - در صورتی که مشتری در صندوق حساب داشته باشد
 */
@Data
public class CustomerInfoResDto implements Serializable {
    /**
     * نام مشتری
     */
    private String firstName;
    /**
     * نام خانوادگی مشتری
     */
    private String lastName;

    /**
     * نام پدر مشتری
     */
    private String fatherName;

    /**
     * تاریخ تولد مشتری (شمسی)
     */
    private String birthDate;

    /**
     * کد ملی مشتری
     */
    private String nationalCode;

    /**
     * شماره تلفن همراه مشتری
     */
    private String mobileNumber;

    /**
     * ایمیل مشتری
     */
    private String email;

    /**
     * شماره حساب مشتری
     */
    private String accountNumber;

    /**
     * شماره شبا مشتری
     */
    private String iban;

    /**
     * نام بانک
     */
    private String bankName;
}
