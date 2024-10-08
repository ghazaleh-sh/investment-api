package ir.co.sadad.investment.services.dao;

import ir.co.sadad.investment.common.enumurations.TransactionStatus;
import ir.co.sadad.investment.common.exception.InvestmentException;
import ir.co.sadad.investment.dto.moneyTransfer.PaymentInquiryResDto;
import ir.co.sadad.investment.dto.moneyTransfer.SendClientReqDto;
import ir.co.sadad.investment.entities.InvestmentPayment;
import ir.co.sadad.investment.mapper.PaymentMapper;
import ir.co.sadad.investment.repositories.InvestmentPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * DAO for manage payment
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class InvestmentPaymentService {

    private final InvestmentPaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    /**
     * get payment based on instructionID
     *
     * @param instructionId id of instruction
     * @return saved payment
     */
    public InvestmentPayment getBy(String instructionId) {
        return paymentRepository.findByInstructionIdentification(instructionId)
                .orElseThrow(() -> new InvestmentException("BUSINESS_PAYMENT_NOT_FOUND", HttpStatus.NOT_FOUND));
    }

    /**
     * create first payment record before calling MoneyTransfer client service
     *
     * @param clientReqDto req for create payment
     * @return saved Payment
     */
    public InvestmentPayment createPaymentRequest(SendClientReqDto clientReqDto) {
        InvestmentPayment paymentToSave = paymentMapper.toEntity(clientReqDto);
        return paymentRepository.saveAndFlush(paymentToSave);
//         return paymentMapper.toDto(paymentToSave);
    }


    /**
     * update payment based on request of user
     *
     * @param instructionId id of payment
     * @param paymentResDto response of successful payment
     */
    public InvestmentPayment updatePaymentSuccess(String instructionId, PaymentInquiryResDto paymentResDto) {
        InvestmentPayment savedPayment = getBy(instructionId);
        savedPayment.setInitiationDate(paymentResDto.getInitiationDate());
        savedPayment.setTransactionStatus(paymentResDto.getTransactionStatus());
        savedPayment.setTraceId(paymentResDto.getTraceId());
        savedPayment.setExecutionDate(paymentResDto.getExecutionDate());
//        savedPayment.setTransactionId(paymentResDto.getTransactionId());
        return paymentRepository.saveAndFlush(savedPayment);

    }

    /**
     * update payment based on request of user
     *
     * @param instructionId id of payment
     */
    public InvestmentPayment updatePaymentStatus(String instructionId, TransactionStatus status) {
        InvestmentPayment savedPayment = getBy(instructionId);
        savedPayment.setTransactionStatus(status);
        return paymentRepository.saveAndFlush(savedPayment);

    }
}
