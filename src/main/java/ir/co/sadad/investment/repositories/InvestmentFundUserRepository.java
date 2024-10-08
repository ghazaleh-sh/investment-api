package ir.co.sadad.investment.repositories;

import ir.co.sadad.investment.entities.InvestmentFundUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvestmentFundUserRepository extends JpaRepository<InvestmentFundUser, Long>, JpaSpecificationExecutor<InvestmentFundUser> {
    Optional<List<InvestmentFundUser>> findByNationalCode(String nationalCode);

    Optional<InvestmentFundUser> findByNationalCodeAndFundId(String nationalCode, String fundId);

    Boolean existsByNationalCodeAndFundId(String nationalCode, String fundId);
}
