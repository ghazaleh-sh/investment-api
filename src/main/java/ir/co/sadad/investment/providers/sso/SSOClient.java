package ir.co.sadad.investment.providers.sso;

import ir.co.sadad.investment.dto.sso.SSOTokenDto;
import ir.co.sadad.investment.providers.sso.configs.SSOBasicAuthentication;
import ir.co.sadad.investment.providers.sso.configs.SSOClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(value = "SSOClient",
        url = "${sso.base_url}",
        configuration = {SSOClientConfig.class, SSOBasicAuthentication.class})
public interface SSOClient {

    @RequestMapping(
            method = RequestMethod.POST,
            value = "${sso.get_token_path}",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    SSOTokenDto geToken(@RequestBody Map<String, ?> form);
}
