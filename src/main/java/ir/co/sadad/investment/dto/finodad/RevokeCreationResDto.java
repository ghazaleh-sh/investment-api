package ir.co.sadad.investment.dto.finodad;

import lombok.Data;

@Data
public class RevokeCreationResDto extends CreationBaseResponseDto {
    /**
     * تعداد واحدهایی که مشتری برای ابطال درخواست داده است
     */
    private Integer fundUnit;
}
