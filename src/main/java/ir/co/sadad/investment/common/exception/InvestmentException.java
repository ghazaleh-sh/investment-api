package ir.co.sadad.investment.common.exception;

import ir.co.sadad.investment.common.exception.handler.dtos.GeneralSubErrorResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.util.List;

/**
 * main exception in project
 */
@Getter
public class InvestmentException extends RuntimeException {

    private final HttpStatusCode httpStatus;
    private Object[] parameters;
    private final List<GeneralSubErrorResponseDto> subErrors;
    private final String code;

    public InvestmentException(String message, HttpStatusCode httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.subErrors = null;
        this.code = null;
    }

    public InvestmentException(String message,
                               HttpStatusCode httpStatus,
                               Object... descriptionParameters) {
        super(message);
        this.httpStatus = httpStatus;
        this.parameters = descriptionParameters;
        this.subErrors = null;
        this.code = null;
    }

    public InvestmentException(String message,
                               List<GeneralSubErrorResponseDto> subErrors,
                               HttpStatusCode httpStatus) {
        super(message);
        this.subErrors = subErrors;
        this.httpStatus = httpStatus;
        this.code = null;
    }

    // code added to separate job exceptions from others
    public InvestmentException(String message, HttpStatusCode httpStatus, String code) {
        super(message);
        this.httpStatus = httpStatus;
        this.subErrors = null;
        this.code = code;
    }
}
