package ir.co.sadad.investment.providers.finodad;

import ir.co.sadad.investment.dto.finodad.*;
import ir.co.sadad.investment.providers.finodad.configs.FinodadClientConfig;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "FinodadClient", url = "${finodad.base-url}", configuration = {FinodadClientConfig.class})
public interface FinodadClient {

    @RequestMapping(method = RequestMethod.POST, value = "${finodad.login}")
    FinodadBaseResponseDto<LoginResponseDataDto> login(@RequestBody LoginRequestDto createSignatureRequestDto);

    @Cacheable(cacheNames = "funds", keyGenerator = "customKeyGenerator")
    @RequestMapping(method = RequestMethod.GET, value = "${finodad.funds}")
    FinodadBaseResponseDto<FundListResponseDto> fundList(@RequestHeader("Authorization") String finodadToken);

    @RequestMapping(method = RequestMethod.GET, value = "${finodad.fund_info}" + "/{fundId}")
    FinodadBaseResponseDto<FundDetailInfoResDto> fundInfo(@RequestHeader("Authorization") String finodadToken,
                                                          @PathVariable("fundId") Integer fundId);


    @RequestMapping(method = RequestMethod.GET, value = "${finodad.customer_balance}" + "/{fundId}" + "/{nationalCode}")
    FinodadBaseResponseDto<CustomerBalanceResDto> customerBalance(@RequestHeader("Authorization") String finodadToken,
                                                                  @PathVariable("fundId") Integer fundId,
                                                                  @PathVariable("nationalCode") String nationalCode);

    @RequestMapping(method = RequestMethod.GET, value = "${finodad.customer_info}" + "/{fundId}" + "/{nationalCode}")
    FinodadBaseResponseDto<CustomerInfoResDto> customerInfo(@RequestHeader("Authorization") String finodadToken,
                                                            @PathVariable("fundId") Integer fundId,
                                                            @PathVariable("nationalCode") String nationalCode);


    @RequestMapping(method = RequestMethod.GET, value = "${finodad.customer_inquiry}" + "/{fundId}" + "/{nationalCode}")
    FinodadBaseResponseDto<CustomerInquiryResDto> customerInquiry(@RequestHeader("Authorization") String finodadToken,
                                                                  @PathVariable("fundId") Integer fundId,
                                                                  @PathVariable("nationalCode") String nationalCode);


    @RequestMapping(method = RequestMethod.GET, value = "${finodad.order_history}" + "/{fundId}" + "/{nationalCode}")
    FinodadBaseResponseDto<CustomerOrderHistoryResDto> orderHistory(@RequestHeader("Authorization") String finodadToken,
                                                                    @PathVariable("fundId") Integer fundId,
                                                                    @PathVariable("nationalCode") String nationalCode,
                                                                    @RequestParam("page") Integer page,
                                                                    @RequestParam("size") Integer size);


    @RequestMapping(method = RequestMethod.GET, value = "${finodad.statement}" + "/{fundId}" + "/{nationalCode}")
    FinodadBaseResponseDto<CustomerStatementResDto> statement(@RequestHeader("Authorization") String finodadToken,
                                                                    @PathVariable("fundId") Integer fundId,
                                                                    @PathVariable("nationalCode") String nationalCode);


    @RequestMapping(method = RequestMethod.POST, value = "${finodad.sejam_otp}")
    FinodadBaseResponseDto<ObjectUtils.Null> sejamOtp(@RequestHeader("Authorization") String finodadToken,
                                                      @RequestBody FundBasicReqDto sejamReq);

    @RequestMapping(method = RequestMethod.POST, value = "${finodad.sejam_otp}")
    FinodadBaseResponseDto<ObjectUtils.Null> register(@RequestHeader("Authorization") String finodadToken,
                                                      @RequestBody CustomerRegisterReqDto req);

    @RequestMapping(method = RequestMethod.POST, value = "${finodad.uuid}")
    FinodadBaseResponseDto<GenerateUuidResDto> generateUuid(@RequestHeader("Authorization") String finodadToken,
                                                            @RequestBody() GenerateUuidReqDto uuidReqDto);

    @RequestMapping(method = RequestMethod.POST, value = "${finodad.issue_uuid}")
    FinodadBaseResponseDto<GenerateUuidResDto> generateIssueUuid(@RequestHeader("Authorization") String finodadToken,
                                                            @RequestBody() GenerateIssueUuidReqDto uuidReqDto);


    @RequestMapping(method = RequestMethod.POST, value = "${finodad.create_issue}")
    FinodadBaseResponseDto<IssueCreationResDto> createIssue(@RequestHeader("Authorization") String finodadToken,
                                                            @RequestBody IssueCreationReqDto creationReqDto);

    @RequestMapping(method = RequestMethod.GET, value = "${finodad.inquiry_issue}" + "/{uuid}")
    FinodadBaseResponseDto<IssueInquiryResDto> inquiryIssue(@RequestHeader("Authorization") String finodadToken,
                                                            @PathVariable("uuid") String uuid);

    @RequestMapping(method = RequestMethod.POST, value = "${finodad.cancel_issue}")
    FinodadBaseResponseDto<ObjectUtils.Null> cancelIssue(@RequestHeader("Authorization") String finodadToken,
                                                          @RequestBody CancelReqDto cancelReqDto);

    @RequestMapping(method = RequestMethod.POST, value = "${finodad.create_revoke}")
    FinodadBaseResponseDto<RevokeCreationResDto> createRevoke(@RequestHeader("Authorization") String finodadToken,
                                                              @RequestBody RevokeCreationReqDto creationReqDto);

    @RequestMapping(method = RequestMethod.GET, value = "${finodad.inquiry_revoke}" + "/{uuid}")
    FinodadBaseResponseDto<RevokeInquiryResDto> inquiryRevoke(@RequestHeader("Authorization") String finodadToken,
                                                              @PathVariable("uuid") String uuid);

    @RequestMapping(method = RequestMethod.POST, value = "${finodad.cancel_revoke}")
    FinodadBaseResponseDto<ObjectUtils.Null> cancelRevoke(@RequestHeader("Authorization") String finodadToken,
                                                          @RequestBody CancelReqDto cancelReqDto);

    @RequestMapping(method = RequestMethod.POST, value = "${finodad.otp_revoke}")
    FinodadBaseResponseDto<ObjectUtils.Null> otpRevoke(@RequestHeader("Authorization") String finodadToken,
                                                       @RequestBody RevokeOtpReqDto otpReqDto);

    @RequestMapping(method = RequestMethod.POST, value = "${finodad.cancel_otp}")
    FinodadBaseResponseDto<ObjectUtils.Null> cancelOtp(@RequestHeader("Authorization") String finodadToken,
                                                       @RequestBody CancelOtpReqDto cancelOtpReqDto);

}
