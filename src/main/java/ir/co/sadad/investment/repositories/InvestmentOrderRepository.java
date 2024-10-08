package ir.co.sadad.investment.repositories;

import ir.co.sadad.investment.common.enumurations.FundCreationState;
import ir.co.sadad.investment.common.enumurations.JobStatus;
import ir.co.sadad.investment.entities.InvestmentFundUser;
import ir.co.sadad.investment.entities.InvestmentOrder;
import ir.co.sadad.investment.entities.InvestmentPayment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvestmentOrderRepository extends JpaRepository<InvestmentOrder, Long>, JpaSpecificationExecutor<InvestmentOrder> {

    Optional<List<InvestmentOrder>> findByFundUser(InvestmentFundUser fundUser);

    Optional<InvestmentOrder> findByUuid(String uuid);

    Optional<InvestmentOrder> findByPayment(InvestmentPayment payment);

    static Specification<InvestmentOrder> withJobStatusFilter(JobStatus jobStatus) {
        return (root, query, criteriaBuilder) -> {
            query.where(criteriaBuilder.isNull(root.get("jobStatus")));
            query.where(criteriaBuilder.or(query.getRestriction(),
                    criteriaBuilder.notEqual(root.get("jobStatus"), jobStatus)));

            return query.getRestriction();
        };
    }

    static Specification<InvestmentOrder> orderStatusFilter() {
        Specification<InvestmentOrder> withOrderStatusFilter = (root, query, criteriaBuilder) ->
                root.get("status").in(List.of(new FundCreationState[]
                        {FundCreationState.ISSUE_BY_JOB, FundCreationState.PAYMENT_BY_JOB, FundCreationState.REVOKE_BY_JOB}));

        return Specification.where(InvestmentOrderRepository.withJobStatusFilter(JobStatus.SUCCEEDED))
                .and(withOrderStatusFilter);

    }

}
