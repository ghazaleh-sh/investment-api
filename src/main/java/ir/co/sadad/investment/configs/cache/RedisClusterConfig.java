package ir.co.sadad.investment.configs.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aswzen
 */
@Component
@ConfigurationProperties(prefix = "spring.redis.sentinel")
@Data
@Order(0)
public class RedisClusterConfig {

    private List<String> nodes;
    private String master;
    private String password;

    public Iterable<RedisNode> getRedisNodes() {
        return this.getNodes().stream().map(item -> new RedisNode(item.split(":")[0], Integer.parseInt(item.split(":")[1]))).collect(Collectors.toList());
    }

}