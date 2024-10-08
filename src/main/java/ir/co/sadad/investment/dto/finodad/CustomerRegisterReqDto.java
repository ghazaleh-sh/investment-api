package ir.co.sadad.investment.dto.finodad;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * ثبت نام مشتری سجامی
 */
@Data
@SuperBuilder
public class CustomerRegisterReqDto extends FundBasicReqDto {
    /**
     * کد اعتبارسنجی که از طریق سرویس ارسال کد سجام برای مشتری ارسال میشود
     */
    private String otp;
}
