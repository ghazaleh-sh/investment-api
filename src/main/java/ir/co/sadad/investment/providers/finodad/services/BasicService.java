package ir.co.sadad.investment.providers.finodad.services;

import ir.co.sadad.investment.common.Empty;
import ir.co.sadad.investment.common.exception.FinodadException;
import ir.co.sadad.investment.common.exception.InvestmentException;
import ir.co.sadad.investment.common.exception.PreconditionFailedException;
import ir.co.sadad.investment.common.util.ConverterHelper;
import ir.co.sadad.investment.dto.finodad.*;
import ir.co.sadad.investment.providers.finodad.FinodadClient;
import ir.co.sadad.investment.services.dao.InvestmentLogService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static ir.co.sadad.investment.common.Constants.BEARER__PREFIX;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicService {
    private static String accessToken;
    /**
     * valid date is in second
     */
    private static Long validDate;

    @Value(value = "${finodad.username}")
    private String userName;
    @Value(value = "${finodad.password}")
    private String password;

    private final FinodadClient finodadClient;

    private final InvestmentLogService logService;


    public String getToken() {
        if (isTokenEmpty() || isTokenShouldBeRefreshed()) {
            callLogin();
        }
        return accessToken;
    }


    /**
     * time from Finodad is in Timestamp- millisecond and must compare with system current date , if system is larger than Finodad then must get new token
     *
     * @return should call login service or not
     */
    static synchronized boolean isTokenShouldBeRefreshed() {
        return validDate < System.currentTimeMillis() / 1000;
    }

    static synchronized boolean isTokenEmpty() {
        return validDate == null
                || Empty.isEmpty(accessToken);
    }


    private void callLogin() {
        try {
            LoginRequestDto requestDto = LoginRequestDto.builder()
                    .password(password)
                    .username(userName)
                    .build();


            FinodadBaseResponseDto<LoginResponseDataDto> response = finodadClient.login(requestDto);
            if (response.isSuccess()) {
                validDate = response.getData().getExpireTime();
                accessToken = BEARER__PREFIX + response.getData().getAccessToken();
                log.info("Successfully logged in to finodad and validate time is =>>>>> {}", response.getData().getExpireTime());
            } else {
                throw new InvestmentException(response.getErrorDetail().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }


        } catch (Exception e) {
            log.error("FINODAD LOGIN SERVICE ERROR IS >>>>>>>>> {0}", e);
            throw new InvestmentException("BUSINESS_LOGIN_UNKNOWN_PROBLEM_FINODAD", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @SneakyThrows
    public String generateUuid(String nationalCode) {
        FinodadBaseResponseDto<GenerateUuidResDto> uuidRes = finodadClient.generateUuid(getToken(),
                GenerateUuidReqDto.builder().nationalCode(nationalCode).build());

        if (uuidRes.isSuccess()) {
            return uuidRes.getData().getUuid();

        } else throw new FinodadException("BUSINESS_UUID_GENERATION_FAILED",
                uuidRes.getErrorDetail().getMessage(),
                uuidRes.getErrorDetail().getCode(),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * method for generate uuid
     * <pre>
     *     this method will generate uuid for issue service ,
     * </pre>
     *
     * @param nationalCode ssn of user
     * @param amount       amount
     * @param findId       if of fund
     * @return generated uuid
     */
    @SneakyThrows
    public String generateUuid(String nationalCode, String amount, String findId) {
        FinodadBaseResponseDto<GenerateUuidResDto> uuidRes = finodadClient.generateIssueUuid(getToken(),
                GenerateIssueUuidReqDto.builder()
                        .nationalCode(nationalCode)
                        .amount(amount)
                        .fundId(findId)
                        .build());

        if (uuidRes.isSuccess()) {
            return uuidRes.getData().getUuid();

        } else throw new FinodadException("BUSINESS_UUID_GENERATION_FAILED",
                uuidRes.getErrorDetail().getMessage(),
                uuidRes.getErrorDetail().getCode(),
                HttpStatus.BAD_REQUEST);
    }

    public void cancelOtp(String uuid, CancelOtpReqDto cancelOtpReqDto) {
        String jsonData = "";
        FinodadBaseResponseDto<ObjectUtils.Null> otpRes;
        try {
            otpRes = finodadClient.cancelOtp(getToken(), cancelOtpReqDto);
            jsonData = ConverterHelper.convertResponseToJson(otpRes);
        } catch (Exception e) {
            logService.saveLogs(e.getClass().getName(), e.getMessage(),
                    uuid, jsonData, "cancelOtp", "EXCEPTION");

            throw new InvestmentException("BUSINESS_CANCEL_OTP_FAILED", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (otpRes.isSuccess()) {
            logService.saveLogs("", "",
                    uuid, jsonData, "cancelOtp", "SUCCESSFUL");

            throw new PreconditionFailedException();
        } else {
            logService.saveLogs("", otpRes.getErrorDetail() != null ? otpRes.getErrorDetail().getMessage() : "",
                    uuid, jsonData, "cancelOtp", "UNSUCCESSFUL");

            throw new FinodadException("BUSINESS_CANCEL_OTP_FAILED",
                    otpRes.getErrorDetail().getMessage(),
                    otpRes.getErrorDetail().getCode(),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
