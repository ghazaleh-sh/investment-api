package ir.co.sadad.investment.providers.moneyTransfer.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import ir.co.sadad.investment.common.exception.InvestmentException;
import ir.co.sadad.investment.common.exception.MoneyTransferException;
import ir.co.sadad.investment.common.exception.PreconditionFailedException;
import ir.co.sadad.investment.common.exception.handler.dtos.GeneralSubErrorResponseDto;
import ir.co.sadad.investment.common.util.ConverterHelper;
import ir.co.sadad.investment.dto.moneyTransfer.PaymentErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PaymentClientErrorDecoderConfig implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        PaymentErrorResponseDto responseBody = null;

        if (response.status() == HttpStatus.UNAUTHORIZED.value())
            throw new InvestmentException("BUSINESS_PAYMENT_UNAUTHORIZED", HttpStatus.UNAUTHORIZED);

        try {
            log.info("PAYMENT BODY: {}", response.body().asInputStream());
            InputStream body = response.body().asInputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            responseBody = objectMapper.readValue(body, PaymentErrorResponseDto.class);
        } catch (Exception e) {
            throw new InvestmentException("BUSINESS_UNKNOWN_PAYMENT_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (responseBody.getStatus() != null && HttpStatus.PRECONDITION_FAILED.isSameCodeAs(HttpStatus.valueOf(responseBody.getStatus()))) {
            return new PreconditionFailedException();
        }
        String jsonData = ConverterHelper.convertResponseToJson(responseBody);
        if (responseBody.getSubErrors() != null && !responseBody.getSubErrors().isEmpty()) {
            List<GeneralSubErrorResponseDto> moneyTransferSubErrors = new ArrayList<>();
            responseBody.getSubErrors().forEach(e -> {
                GeneralSubErrorResponseDto generalSubErrorResponseDto = new GeneralSubErrorResponseDto();
                generalSubErrorResponseDto.setCode(e.getCode());
                generalSubErrorResponseDto.setMessage(e.getMessage());
                generalSubErrorResponseDto.setLocalizedMessage(e.getLocalizedMessage());
                moneyTransferSubErrors.add(generalSubErrorResponseDto);
            });

            return new MoneyTransferException(responseBody.getMessage(),
                    responseBody.getLocalizedMessage(),
                    moneyTransferSubErrors,
                    HttpStatus.valueOf(responseBody.getStatus()),
                    jsonData);
        }
        return new MoneyTransferException(responseBody.getMessage(),
                responseBody.getLocalizedMessage(),
                HttpStatus.valueOf(responseBody.getStatus()),
                jsonData);

    }
}
