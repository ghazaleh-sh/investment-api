package ir.co.sadad.investment.common.exception;


import lombok.Getter;

@Getter
public class PreconditionFailedException extends RuntimeException {
    private final Object extraData;
    private final String message;

    public PreconditionFailedException() {
        this.message = "OTP_REQUIRED";
        this.extraData = null;
    }

    public PreconditionFailedException(Object extraData) {
        this.extraData = extraData;
        this.message = "OTP_REQUIRED";
    }
}
