package ir.co.sadad.investment.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    /**
     * this Bean controls the concurrency level with the specified pool size and queue capacity.
     * <p>
     * to limit the maximum number of concurrent threads to 10
     * and limit the queue size to 500
     */
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("investment-payment");
        executor.initialize();
        return executor;
    }
}
