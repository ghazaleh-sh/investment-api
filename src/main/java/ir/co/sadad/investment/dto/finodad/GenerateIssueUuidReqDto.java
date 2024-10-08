package ir.co.sadad.investment.dto.finodad;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * request for generate uuid for Issue services
 */
@Builder
@Data
public class GenerateIssueUuidReqDto implements Serializable {

    private String nationalCode;

    /**
     * fundID
     * <pre>
     *     use in generate uuid for issue
     * </pre>
     */
    private String fundId;

    /**
     * amount
     * <pre>
     *     use in generate uuid for issue
     * </pre>
     */
    private String amount;
}
