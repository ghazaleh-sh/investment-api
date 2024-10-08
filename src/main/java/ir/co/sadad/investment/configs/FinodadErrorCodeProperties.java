package ir.co.sadad.investment.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "finodad")
@Data
public class FinodadErrorCodeProperties {

    List<Integer> job_error_codes;
}
