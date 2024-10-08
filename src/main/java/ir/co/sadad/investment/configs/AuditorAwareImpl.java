package ir.co.sadad.investment.configs;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

import static ir.co.sadad.investment.common.Constants.SSN;


/**
 * config for token authorization client
 */
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    /**
     * method for getting current auditor ,
     *
     * @return ssn of bmi Identity Token From Client.
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        String auditSSN = null;

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = null;
        try {
            httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        } catch (NullPointerException ex) {
            // it doesn't come from http request
            httpServletRequest = null;
        }

        if (httpServletRequest != null && !httpServletRequest.getServletPath().contains("/job")) {
            auditSSN = httpServletRequest.getHeader(SSN);
        } else { // if requests are outside an actual web request, such as job and scheduled executor
            auditSSN = "1254";
        }

        return auditSSN == null ? Optional.empty() : Optional.of(auditSSN);
    }

}