package ir.co.sadad.investment.dto.finodad;

import lombok.Data;

@Data
public class RevokeInquiryResDto extends InquiryBasicResponseDto {
    /**
     * تعداد واحدهایی که مشتری برای ابطال درخواست داده است
     */
    private Integer fundUnit;
}
