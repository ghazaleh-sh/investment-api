package ir.co.sadad.investment.providers.eb;

import ir.co.sadad.investment.dto.eb.EbAccountResponseDto;
import ir.co.sadad.investment.providers.eb.configs.EBClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "EBClient", url = "${eb.base-url}", configuration = {EBClientConfig.class})
public interface EBClient {

    @RequestMapping(method = RequestMethod.GET, value = "${eb.get_accounts_path}")
    List<EbAccountResponseDto> getOwnerAccounts(@RequestParam("ssn") String ownerSsn);
}
