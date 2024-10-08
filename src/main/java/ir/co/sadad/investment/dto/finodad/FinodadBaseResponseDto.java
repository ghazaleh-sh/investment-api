package ir.co.sadad.investment.dto.finodad;

import lombok.Data;

/**
 * base response of Finodad service
 *
 * @param <T>
 */
@Data
public class FinodadBaseResponseDto<T> implements ThirdPartyServicesResponse {
    /**
     * true when everything is OK , false when exception happens
     */
    private boolean success;
    /**
     * response of Finodad
     */
    private T data;

    /**
     * exception of Finodad
     */
    private ErrorDto errorDetail;

    /**
     * tracking code
     */
    private String trackingId;

    /**
     * time of call service
     */
    private String doTime;
}
