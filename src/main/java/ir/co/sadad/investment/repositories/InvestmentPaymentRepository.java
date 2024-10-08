package ir.co.sadad.investment.repositories;

import ir.co.sadad.investment.entities.InvestmentPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestmentPaymentRepository extends JpaRepository<InvestmentPayment, Long>, JpaSpecificationExecutor<InvestmentPayment> {

    Optional<InvestmentPayment> findByInstructionIdentification(String instructionIdentification);
}
