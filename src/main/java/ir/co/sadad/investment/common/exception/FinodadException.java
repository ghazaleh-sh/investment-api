package ir.co.sadad.investment.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

/**
 * exception for Finodad services
 * <pre>
 *     when exception from Finodad services happened , must user this exception
 * </pre>
 */
@Getter
public class FinodadException extends RuntimeException {
    private final HttpStatusCode httpStatus;
    private final String finodadLocalizedMessage;
    private final Integer finodadCode;

    public FinodadException(String message,
                            String finodadLocalizedMessage,
                            Integer finodadCode,
                            HttpStatusCode httpStatus) {
        super(message);
        this.finodadLocalizedMessage = finodadLocalizedMessage;
        this.finodadCode = finodadCode;
        this.httpStatus = httpStatus;
    }
}
