package ir.co.sadad.investment.dto.finodad;

import lombok.Builder;

/**
 * درخواست لغو سفارش ثدور یا ابطال جهت دربافت otp
 */
@Builder
public record CancelOtpReqDto(String nationalCode,
                              String fundId) {
}
