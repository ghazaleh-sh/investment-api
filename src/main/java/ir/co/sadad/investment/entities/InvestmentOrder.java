package ir.co.sadad.investment.entities;

import ir.co.sadad.investment.common.enumurations.FundCreationState;
import ir.co.sadad.investment.common.enumurations.FundRequestType;
import ir.co.sadad.investment.common.enumurations.JobStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "INVESTMENT_ORDER")
public class InvestmentOrder extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "UUID", nullable = false)
    private String uuid;
    @Column(name = "ORDER_ID")
    private String orderId;
    @Column(name = "AMOUNT")
    private Long amount;
    @Enumerated(EnumType.STRING)
    @Column(name = "REQUEST_TYPE")
    private FundRequestType requestType;
    @Column(name = "CREATED_DATE")
    private String createdDate;
    @Column(name = "FUND_UNIT")
    private Integer fundUnit;
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private FundCreationState status;
    @Column(name = "ISSUED_DATE")
    private String issuedDate;
    @Column(name = "RESPONSE_TIME")
    private String responseTime;
    @Column(name = "JOB_DURATION")
    private String jobDuration;
    @Enumerated(EnumType.STRING)
    @Column(name = "JOB_STATUS")
    private JobStatus jobStatus;

    @ManyToOne
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FKINVESTMENT_ORDER_TO_FUND_USER"))
    private InvestmentFundUser fundUser;

    @OneToOne
    @JoinColumn(name = "PAYMENT_ID", foreignKey = @ForeignKey(name = "FKINVESTMENT_ORDER_TO_PAYMENT"))
    private InvestmentPayment payment;

}
