package ir.co.sadad.investment.services.dao;

import ir.co.sadad.investment.entities.InvestmentLog;
import ir.co.sadad.investment.repositories.InvestmentLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

/**
 * service for manage logs
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class InvestmentLogService {

    private final MessageSource messageSource;
    private final InvestmentLogRepository investmentLogRepository;

    /**
     * save logs in db
     *
     * @param eName      errorClass
     * @param eMsg       message
     * @param orderId    id of order - points to <code> InvestmentOrder </code>
     * @param jsonData   data that has exception
     * @param methodName name of caller method
     * @param status     status  of order
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLogs(String eName, String eMsg, String orderId, String jsonData, String methodName, String status) {

        String localizedMessage;
        try {
            localizedMessage = messageSource.getMessage(eMsg, null, Locale.of("fa"));
        } catch (NoSuchMessageException e) {
            localizedMessage = eMsg;
        }

        try {
            investmentLogRepository.save(InvestmentLog
                    .builder()
                    .errorClass(eName)
                    .errorMessage(localizedMessage)
                    .orderId(orderId)
                    .methodName(methodName)
                    .serviceResponse(jsonData)
                    .status(status)
                    .build());
        } catch (Exception ex) {
            log.error("--------- error doesn't save into InvestmentLog table with message: {}", ex.getMessage());

        }

    }
}
