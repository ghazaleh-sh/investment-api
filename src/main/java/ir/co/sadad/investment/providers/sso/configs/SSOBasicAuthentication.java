package ir.co.sadad.investment.providers.sso.configs;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class SSOBasicAuthentication {

    @Value("${sso.client_id}")
    String clientName;

    @Value("${sso.client_sec}")
    String clientPassword;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(clientName, clientPassword);
    }
}
