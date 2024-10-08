package ir.co.sadad.investment.dto.finodad;

import lombok.Builder;

/**
 * درخواست لغو درخواست صدور/ ابطال
 */
@Builder
public record CancelReqDto(String uuid, String nationalCode, String fundId, String otp) {

}
