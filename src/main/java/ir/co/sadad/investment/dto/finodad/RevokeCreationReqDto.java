package ir.co.sadad.investment.dto.finodad;

import lombok.Data;

@Data
public class RevokeCreationReqDto extends CreationBaseRequestDto {
    /**
     * تعداد واحدهایی که مشتری برای ابطال درخواست داده است
     */
    private Integer fundUnit;

    /**
     * کد یکبار مصرف ابطال از سرویس کد یک بار مصرف ابطال
     */
    private String otp;
}
