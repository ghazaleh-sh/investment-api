package ir.co.sadad.investment.common.enumurations;

/**
 * payment history status ,
 * <pre>
 *     use in search filter of payment history
 *     UNKNOWN in this search filter equal to Unknown_payment and Unknown_inquiry in payment business
 * </pre>
 */
public enum PaymentHistoryStatus {

    UNKNOWN,
    SUCCESS,
    FAILED
}
