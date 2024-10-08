package ir.co.sadad.investment.dto.finodad;

import lombok.Data;

/**
 * response of Login
 */
@Data
public class LoginResponseDataDto {
    /**
     * token for use in other services
     */
    private String accessToken;

    /**
     * expire time of token
     */
    private Long expireTime;
}
