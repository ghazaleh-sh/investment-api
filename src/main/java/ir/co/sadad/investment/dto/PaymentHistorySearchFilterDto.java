package ir.co.sadad.investment.dto;

import ir.co.sadad.investment.common.enumurations.PaymentHistoryStatus;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

/**
 * dto for manage payment history search filter ,
 * <pre>
 *     search filters from controller with this dto will transfer to service.
 * </pre>
 */
@Builder
@Getter
public class PaymentHistorySearchFilterDto {


    /**
     * specification for pageable parameter such az pageSize and pageNumber
     */
    private Pageable pageable;

    /**
     * amount_gt
     */
    private Long amountFrom;

    /**
     * amount_lt
     */
    private Long amountTo;

    /**
     * date_gt
     */
    private String dateFrom;

    /**
     * date_lt
     */
    private String dateTo;

    private PaymentHistoryStatus status;

    private Integer fundId;

    private String ssn;


}
