package ir.co.sadad.investment.dto.finodad;

import lombok.Data;

import java.io.Serializable;

/**
 * پتسخ سرویس استعلام دارایی های مشتری
 */
@Data
public class CustomerBalanceResDto implements Serializable {
    /**
     * تاریخ آخرین گواهی صادر شده
     * )YYYY/MM/DD(
     */
    private String licenseDate;

    /**
     * دارایی مشتری بر اساس تعداد واحد صندوق
     */
    private Integer fundUnit;

    /**
     * معادل ریالی دارایی مشتری
     */
    private Long asset;
}
