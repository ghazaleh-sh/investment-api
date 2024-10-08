package ir.co.sadad.investment.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "INVESTMENT_LOG")
public class InvestmentLog extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ERROR_CLASS")
    private String errorClass;

    @Column(name = "ERROR_MESSAGE", length = 1000)
    private String errorMessage;

    @Column(name = "ORDER_ID")
    private String orderId;

    @Column(name = "METHOD_NAME")
    private String methodName;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "SERVICE_RESPONSE", columnDefinition = "CLOB")
    private String serviceResponse;
}
