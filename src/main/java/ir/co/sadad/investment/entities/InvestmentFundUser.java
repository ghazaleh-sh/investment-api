package ir.co.sadad.investment.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "INVESTMENT_FUND_USER")
public class InvestmentFundUser extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NATIONAL_CODE", nullable = false, columnDefinition = "char(10)")
    private String nationalCode;

    @Column(name = "FUND_ID", nullable = false)
    private String fundId;
}
