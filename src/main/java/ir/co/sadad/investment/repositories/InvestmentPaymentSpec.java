package ir.co.sadad.investment.repositories;

import ir.co.sadad.investment.common.Empty;
import ir.co.sadad.investment.common.enumurations.PaymentHistoryStatus;
import ir.co.sadad.investment.common.enumurations.TransactionStatus;
import ir.co.sadad.investment.dto.PaymentHistorySearchFilterDto;
import ir.co.sadad.investment.entities.InvestmentOrder;
import ir.co.sadad.investment.entities.InvestmentPayment;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

/**
 * spec for payment service
 * <pre>
 *     used for payment history service
 * </pre>
 */
public class InvestmentPaymentSpec {

    private static final String PAYMENT_STATUS = "transactionStatus";
    private static final String PAYMENT_AMOUNT = "amount";
    private static final String PAYMENT = "payment";
    private static final String PAYMENT_DATE = "initiationDate";
    private static final String USER = "fundUser";
    private static final String USER_SSN = "nationalCode";
    private static final String USER_FUND = "fundId";


    private InvestmentPaymentSpec() {
        //empty
    }

    public static Specification<InvestmentOrder> filterBy(PaymentHistorySearchFilterDto filter) {
        return Specification
                .where(hasPayment())
                .and(hasUserSSN(filter.getSsn()))
                .and(hasFund(filter.getFundId().toString()))
                .and(hasPaymentStatus(filter.getStatus()))
                .and(hasPaymentDateAfter(filter.getDateFrom()))
                .and(hasPaymentDateBefore(filter.getDateTo()))
                .and(hasPriceGreaterThan(filter.getAmountFrom()))
                .and(hasPriceLessThan(filter.getAmountTo()));
    }

    private static Specification<InvestmentOrder> hasPayment() {
        return (root, query, cb) ->  cb.isNotNull(root.get(PAYMENT));
    }

    private static Specification<InvestmentOrder> hasFund(String fundId) {
        return (root, query, cb) -> Empty.isEmpty(fundId) ? cb.conjunction() : cb.equal(root.get(USER).get(USER_FUND), fundId);
    }

    private static Specification<InvestmentOrder> hasUserSSN(String ssn) {
        return (root, query, cb) -> Empty.isEmpty(ssn) ? cb.conjunction() : cb.equal(root.get(USER).get(USER_SSN), ssn);
    }

    private static Specification<InvestmentOrder> hasPaymentStatus(PaymentHistoryStatus paymentStatus) {


        return (root, query, cb) ->
        {
            Predicate predicate = cb.conjunction();
            if (Empty.isNotEmpty(paymentStatus))
                switch (paymentStatus) {
                    case SUCCESS ->
                            predicate = cb.equal(root.get(PAYMENT).get(PAYMENT_STATUS), TransactionStatus.SUCCEEDED.name());
                    case FAILED ->
                            predicate = cb.equal(root.get(PAYMENT).get(PAYMENT_STATUS), TransactionStatus.FAILED.name());
                    case UNKNOWN ->
                            predicate = cb.or(cb.equal(root.get(PAYMENT).get(PAYMENT_STATUS), TransactionStatus.UNKNOWN_PAYMENT.name())
                                    , cb.equal(root.get(PAYMENT).get(PAYMENT_STATUS), TransactionStatus.UNKNOWN_INQUIRY.name()));
                }
            return predicate;
        };
    }


    private static Specification<InvestmentOrder> hasPaymentDateAfter(String dateFrom) {
        return (root, query, cb) -> Empty.isEmpty(dateFrom) ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get(PAYMENT).get(PAYMENT_DATE), dateFrom);
    }

    private static Specification<InvestmentOrder> hasPaymentDateBefore(String dateTo) {
        return (root, query, cb) -> Empty.isEmpty(dateTo) ? cb.conjunction() : cb.lessThanOrEqualTo(root.get(PAYMENT).get(PAYMENT_DATE), dateTo);
    }

    private static Specification<InvestmentOrder> hasPriceGreaterThan(Long priceFrom) {
        return (root, query, cb) -> Empty.isEmpty(priceFrom) ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get(PAYMENT_AMOUNT), priceFrom);
    }

    private static Specification<InvestmentOrder> hasPriceLessThan(Long priceTo) {
        return (root, query, cb) -> Empty.isEmpty(priceTo) ? cb.conjunction() : cb.lessThanOrEqualTo(root.get(PAYMENT_AMOUNT), priceTo);
    }
}
