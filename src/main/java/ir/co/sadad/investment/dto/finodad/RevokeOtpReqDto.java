package ir.co.sadad.investment.dto.finodad;

import lombok.Builder;

@Builder
public record RevokeOtpReqDto(String nationalCode, String fundId) {
}
