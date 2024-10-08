package ir.co.sadad.investment.dto.finodad;

import lombok.Data;

@Data
public class CreationBaseResponseDto {
    /**
     * شناسه درخواست
     */
    private String orderId;
    private String nationalCode;
    private String additionalData;
}
