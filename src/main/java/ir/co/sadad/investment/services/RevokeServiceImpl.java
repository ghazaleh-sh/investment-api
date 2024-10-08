package ir.co.sadad.investment.services;

import ir.co.sadad.investment.common.enumurations.FundCreationState;
import ir.co.sadad.investment.common.enumurations.JobStatus;
import ir.co.sadad.investment.common.exception.FinodadException;
import ir.co.sadad.investment.common.exception.InvestmentException;
import ir.co.sadad.investment.common.exception.PreconditionFailedException;
import ir.co.sadad.investment.common.util.ConverterHelper;
import ir.co.sadad.investment.common.util.TripleDES;
import ir.co.sadad.investment.configs.FinodadErrorCodeProperties;
import ir.co.sadad.investment.dto.RevokeReqDto;
import ir.co.sadad.investment.dto.RevokeResDto;
import ir.co.sadad.investment.dto.finodad.*;
import ir.co.sadad.investment.dto.moneyTransfer.OtpResDto;
import ir.co.sadad.investment.mapper.OrderMapper;
import ir.co.sadad.investment.providers.finodad.FinodadClient;
import ir.co.sadad.investment.providers.finodad.services.BasicService;
import ir.co.sadad.investment.services.dao.InvestmentLogService;
import ir.co.sadad.investment.services.dao.InvestmentOrdersService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ir.co.sadad.investment.common.Constants.JOB_CODE;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(noRollbackFor = {RuntimeException.class})
public class RevokeServiceImpl implements RevokeService {

    private final FinodadClient finodadClient;
    private final InvestmentOrdersService orderService;
    private final OrderMapper orderMapper;
    private final CustomerFundService customerFundService;
    private final BasicService basicService;
    private final InvestmentLogService logService;
    private final TripleDES tripleDES;
    private final FinodadErrorCodeProperties finodadErrorCodesForJob;

    @Override
    public RevokeResDto createInitialRevoke(RevokeReqDto revokeReqDto, String ssn, String otp) {
        revokeReqDto.setNationalCode(ssn);
//        FundResponseDto fundInfoOfCustomer = customerFundService.getActiveCustomerFund(revokeReqDto.getNationalCode(), revokeReqDto.getFundId());

        String uuid;
        if (otp == null) {
            uuid = basicService.generateUuid(revokeReqDto.getNationalCode());
            revokeReqDto.setUuid(uuid);
            orderService.createRevokeOrder(revokeReqDto);

            revokeRequestOtp(revokeReqDto);

        } else {
            uuid = revokeReqDto.getUuid();
            if (uuid == null)
                throw new InvestmentException("V_UUID_ID_REQUIRE", HttpStatus.BAD_REQUEST);
        }

        return createRevoke(revokeReqDto, otp);

    }

    @Override
    public RevokeResDto createRevoke(RevokeReqDto revokeReqDto, String otp) {
        String jsonData = null;
        String uuid = revokeReqDto.getUuid();
        try {
            RevokeCreationReqDto revokeReq = buildRevokeRequest(revokeReqDto, otp);
            FinodadBaseResponseDto<RevokeCreationResDto> revokeRes = finodadClient.createRevoke(basicService.getToken(), revokeReq);

            jsonData = ConverterHelper.convertResponseToJson(revokeRes);

            if (revokeRes.isSuccess()) {
                RevokeResDto resDto = orderService.updateRevokeOrderWithOrderId(uuid, revokeRes.getData().getOrderId(), JobStatus.SUCCEEDED);
                resDto.setNationalCode(revokeReqDto.getNationalCode());
                logService.saveLogs("", "",
                        uuid, jsonData, "createRevoke", "SUCCESSFUL");
                return resDto;

            } else {
                logService.saveLogs("", revokeRes.getErrorDetail() != null ? revokeRes.getErrorDetail().getMessage() : "",
                        uuid, jsonData, "createRevoke", "UNSUCCESSFUL");

                if (revokeRes.getErrorDetail() != null && finodadErrorCodesForJob.getJob_error_codes().contains(revokeRes.getErrorDetail().getCode())) {
                    return inquiryRevoke(uuid);
                }

                orderService.updateOrderStatus(uuid, FundCreationState.UNSUCCESSFUL, JobStatus.FAILED);
                throw new FinodadException("BUSINESS_REVOKE_UNKNOWN_FAILED",
                        revokeRes.getErrorDetail().getMessage(),
                        revokeRes.getErrorDetail().getCode(),
                        HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            if (!(e instanceof FinodadException || e instanceof InvestmentException)) {
                logService.saveLogs(e.getClass().getName(), e.getMessage(),
                        uuid, jsonData, "createRevoke", "EXCEPTION");

                return inquiryRevoke(uuid);
            }
            throw e;
        }
    }

    @SneakyThrows
    @Override
    public RevokeResDto inquiryRevoke(String uuid) {
        String jsonData = null;
        try {
            FinodadBaseResponseDto<RevokeInquiryResDto> revokeInquiryRes = finodadClient.inquiryRevoke(basicService.getToken(), uuid);
            jsonData = ConverterHelper.convertResponseToJson(revokeInquiryRes);

            if (revokeInquiryRes.isSuccess()) {
                RevokeResDto resDto = orderService.updateRevokeOrderWithOrderId(uuid, revokeInquiryRes.getData().getOrderId(), JobStatus.SUCCEEDED);
                resDto.setNationalCode(revokeInquiryRes.getData().getNationalCode());

                logService.saveLogs("", "",
                        uuid, jsonData, "inquiryRevoke", "SUCCESSFUL");

                return resDto;

            } else {
                logService.saveLogs("", revokeInquiryRes.getErrorDetail() != null ? revokeInquiryRes.getErrorDetail().getMessage() : "",
                        uuid, jsonData, "inquiryRevoke", "UNSUCCESSFUL");

                if (revokeInquiryRes.getErrorDetail() != null && finodadErrorCodesForJob.getJob_error_codes().contains(revokeInquiryRes.getErrorDetail().getCode())) {
                    orderService.updateOrderStatus(uuid, FundCreationState.REVOKE_BY_JOB, JobStatus.FAILED);
                } else
                    orderService.updateOrderStatus(uuid, FundCreationState.UNSUCCESSFUL, JobStatus.FAILED);

                throw new FinodadException("BUSINESS_REVOKE_INQUIRY_UNKNOWN_FAILED",
                        revokeInquiryRes.getErrorDetail().getMessage(),
                        revokeInquiryRes.getErrorDetail().getCode(),
                        HttpStatus.BAD_REQUEST);

            }
        } catch (Exception e) {
            if (!(e instanceof FinodadException)) {
                logService.saveLogs(e.getClass().getName(), e.getMessage(),
                        uuid, jsonData, "inquiryRevoke", "EXCEPTION");
                orderService.updateOrderStatus(uuid, FundCreationState.REVOKE_BY_JOB, JobStatus.FAILED);

                throw new InvestmentException("BUSINESS_REVOKE_UNKNOWN_FAILED", HttpStatus.INTERNAL_SERVER_ERROR, JOB_CODE);
            }
            throw e;
        }
    }

    @Override
    public void cancelRevoke(String uuid, String fundId, String ssn, String otp) {
        orderService.getBy(uuid);

        String jsonData = "";
        if (otp == null)
            basicService.cancelOtp(uuid, CancelOtpReqDto.builder().nationalCode(ssn).fundId(fundId).build());

        else {
            FinodadBaseResponseDto<ObjectUtils.Null> cancelRevokeRes;
            try {
                cancelRevokeRes = finodadClient.cancelRevoke(basicService.getToken(),
                        CancelReqDto.builder()
                                .uuid(uuid)
                                .nationalCode(ssn)
                                .fundId(fundId)
                                .otp(otp).build());

                jsonData = ConverterHelper.convertResponseToJson(cancelRevokeRes);
            } catch (Exception e) {
                logService.saveLogs(e.getClass().getName(), e.getMessage(),
                        uuid, jsonData, "cancelRevoke", "EXCEPTION");
                throw e;//new InvestmentException("BUSINESS_CANCEL_REVOKE_FAILED", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (!cancelRevokeRes.isSuccess()) {
                logService.saveLogs("", cancelRevokeRes.getErrorDetail() != null ? cancelRevokeRes.getErrorDetail().getMessage() : "",
                        uuid, jsonData, "cancelRevoke", "UNSUCCESSFUL");

                throw new FinodadException("BUSINESS_CANCEL_REVOKE_FAILED",
                        cancelRevokeRes.getErrorDetail().getMessage(),
                        cancelRevokeRes.getErrorDetail().getCode(),
                        HttpStatus.BAD_REQUEST);

            } else {
                orderService.updateOrderStatus(uuid, FundCreationState.CANCELED, JobStatus.SUCCEEDED);
                logService.saveLogs("", "",
                        uuid, jsonData, "cancelRevoke", "SUCCESSFUL");
            }
        }
    }


    private void revokeRequestOtp(RevokeReqDto revokeReqDto) {
        String jsonData = "";
        FinodadBaseResponseDto<ObjectUtils.Null> otpRes;
        try {
            otpRes = finodadClient.otpRevoke(basicService.getToken(), RevokeOtpReqDto.builder()
                    .nationalCode(revokeReqDto.getNationalCode())
                    .fundId(revokeReqDto.getFundId()).build());

            jsonData = ConverterHelper.convertResponseToJson(otpRes);
        } catch (Exception e) {
            logService.saveLogs(e.getClass().getName(), e.getMessage(),
                    revokeReqDto.getUuid(), jsonData, "revokeRequestOtp", "EXCEPTION");
            throw new InvestmentException("BUSINESS_REVOKE_OTP_FAILED", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (otpRes.isSuccess()) {
            logService.saveLogs("", "",
                    revokeReqDto.getUuid(), jsonData, "revokeRequestOtp", "SUCCESSFUL");

            throw new PreconditionFailedException(OtpResDto.builder().uuid(revokeReqDto.getUuid()).build());
        } else {
            logService.saveLogs("", otpRes.getErrorDetail() != null ? otpRes.getErrorDetail().getMessage() : "",
                    revokeReqDto.getUuid(), jsonData, "revokeRequestOtp", "UNSUCCESSFUL");

            throw new FinodadException("BUSINESS_REVOKE_OTP_FAILED",
                    otpRes.getErrorDetail().getMessage(),
                    otpRes.getErrorDetail().getCode(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private RevokeCreationReqDto buildRevokeRequest(RevokeReqDto revokeReqDto, String otp) {
        String signature = revokeReqDto.getFundUnit() + "|" + revokeReqDto.getNationalCode() + "|" + revokeReqDto.getFundId() + "|" + revokeReqDto.getUuid();

        RevokeCreationReqDto mappedRevoke = orderMapper.toRevokeClient(revokeReqDto);
        mappedRevoke.setOtp(otp);
        mappedRevoke.setSignature(tripleDES.sign(signature));
        return mappedRevoke;
    }
}
