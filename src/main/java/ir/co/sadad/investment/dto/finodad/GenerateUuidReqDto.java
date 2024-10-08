package ir.co.sadad.investment.dto.finodad;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * request for generate uuid for Finodad service except Issue services
 */
@Builder
@Data
public class GenerateUuidReqDto implements Serializable {

    private String nationalCode;
}
