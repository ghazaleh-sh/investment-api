package ir.co.sadad.investment.dto.moneyTransfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ir.co.sadad.investment.dto.finodad.ThirdPartyServicesResponse;
import lombok.Data;

import java.util.List;

/**
 * response of MoneyTransfer services
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentErrorResponseDto implements ThirdPartyServicesResponse {

    private Integer status;
    private String timestamp;
    private String localizedMessage;
    private String code;
    private String message;
    private List<SubErrors> subErrors;

    @Data
    public static class SubErrors {
        private String code;
        private String message;
        private String localizedMessage;
    }
}
