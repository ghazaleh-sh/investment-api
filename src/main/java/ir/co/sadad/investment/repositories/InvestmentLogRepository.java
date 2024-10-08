package ir.co.sadad.investment.repositories;

import ir.co.sadad.investment.entities.InvestmentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentLogRepository extends JpaRepository<InvestmentLog, Long>, JpaSpecificationExecutor<InvestmentLog> {
}
