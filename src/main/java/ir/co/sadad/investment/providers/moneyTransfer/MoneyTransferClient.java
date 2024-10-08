package ir.co.sadad.investment.providers.moneyTransfer;

import feign.Headers;
import ir.co.sadad.investment.dto.moneyTransfer.PaymentInquiryResDto;
import ir.co.sadad.investment.dto.moneyTransfer.SendClientReqDto;
import ir.co.sadad.investment.dto.moneyTransfer.SendClientResDto;
import ir.co.sadad.investment.providers.moneyTransfer.configs.PaymentClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "MoneyTransferClient", url = "${payment.base_url}", configuration = {PaymentClientConfig.class})
public interface MoneyTransferClient {

    @RequestMapping(method = RequestMethod.POST, value = "${payment.client_send_url}")
    @Headers("Content-Type: application/json")
    SendClientResDto clientSend(@RequestHeader("Authorization") String token,
                                        @RequestBody SendClientReqDto sendClientReqDto);

    @RequestMapping(method = RequestMethod.POST, value = "${payment.client_send_url}")
    @Headers("Content-Type: application/json")
    SendClientResDto clientSendWithCode(@RequestHeader("Authorization") String token,
                                        @RequestBody SendClientReqDto sendClientReqDto,
                                        @RequestParam("authorizationCode") String authorizationCode);


    @RequestMapping(method = RequestMethod.GET, value = "${payment.inquiry_url}")
    PaymentInquiryResDto paymentInquiry(@RequestHeader("Authorization") String token,
                                        @RequestParam("instructionIdentification") String instructionIdentification);
}
