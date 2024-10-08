package ir.co.sadad.investment.dto.finodad;

import lombok.Data;

import java.io.Serializable;

/**
 * پاسخ سرویس لیست صندوق های مجاز
 */
@Data
public class FundListDetailResDto implements Serializable {

    /**
     * شناسه صندوق سرمایه گذاری
     */
    private Integer id;

    /**
     * نام صندوق سرمایه گذاری
     */
    private String name;

    /**
     * شعبه بانک
     */
    private String bankBranch;

    /**
     * نام بانک
     */
    private String bankName;

    /**
     * شماره حساب در بانک
     */
    private String bankAccountNumber;
}
