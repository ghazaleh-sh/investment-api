package ir.co.sadad.investment.dto.sso;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SSOTokenDto {

    @JsonProperty("last_logins")
    private String lastLogins;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("access_token")
    private String accessToken;
}
