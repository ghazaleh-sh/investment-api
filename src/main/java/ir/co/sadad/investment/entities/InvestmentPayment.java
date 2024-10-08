package ir.co.sadad.investment.entities;

import ir.co.sadad.investment.common.enumurations.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "INVESTMENT_PAYMENT")
public class InvestmentPayment extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TRANSACTION_STATUS")
    private TransactionStatus transactionStatus;

    @Column(name = "FROM_ACCOUNT", nullable = false)
    private String fromAccount;

    @Column(name = "TARGET_ACCOUNT", nullable = false)
    private String targetAccount;

    @Column(name = "INITIATION_DATE")
    private String initiationDate;

    @Column(name = "INSTRUCTION_IDENTIFICATION")
    private String instructionIdentification;

    @Enumerated(EnumType.STRING)
    @Column(name = "INSTRUCTION_type", nullable = false)
    private InstructionType instructionType;

    @Column(name = "AMOUNT", nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "CURRENCY", nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "MECHANISM", nullable = false)
    private Mechanism mechanism;

    @Column(name = "TRACE_ID")
    private String traceId;

    @Column(name = "EXECUTION")
    private String executionDate;

    @Column(name = "TRANSACTION_ID")
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "PRODUCT_TYPE")
    private ProductType productType;

    @Column(name = "CREDITPAY_ID", nullable = false)
    private String creditPayId;


}
