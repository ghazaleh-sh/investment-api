package ir.co.sadad.investment.common.exception;

import ir.co.sadad.investment.common.exception.handler.dtos.GeneralSubErrorResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.util.List;

/**
 * exception for moneyTransfer - business exceptions only ,
 * <pre>
 *     this exception has its own handler
 * </pre>
 */
@Getter
public class MoneyTransferException extends RuntimeException {

    private final HttpStatusCode httpStatus;
    private final String localizedMessage;
    private final List<GeneralSubErrorResponseDto> subErrors;
    private final String jsonError;

    public MoneyTransferException(String message,
                                  String localizedMessage,
                                  HttpStatusCode httpStatus,
                                  String jsonError) {
        super(message);
        this.localizedMessage = localizedMessage;
        this.httpStatus = httpStatus;
        this.subErrors = null;
        this.jsonError = jsonError;
    }

    public MoneyTransferException(String message,
                                  String localizedMessage,
                                  List<GeneralSubErrorResponseDto> subErrors,
                                  HttpStatusCode httpStatus,
                                  String jsonError) {
        super(message);
        this.localizedMessage = localizedMessage;
        this.subErrors = subErrors;
        this.httpStatus = httpStatus;
        this.jsonError = jsonError;
    }
}
