package ir.co.sadad.investment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@SpringBootApplication
@EnableCaching
@EnableAsync
public class InvestmentApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(InvestmentApiApplication.class, args);
	}
}
