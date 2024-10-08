package ir.co.sadad.investment.dto.finodad;

import lombok.Builder;
import lombok.Data;

/**
 * request for login
 */
@Data
@Builder
public class LoginRequestDto {
    private String username;
    private String password;
}
