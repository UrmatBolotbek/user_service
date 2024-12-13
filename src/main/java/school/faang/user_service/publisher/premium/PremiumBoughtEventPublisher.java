package school.faang.user_service.publisher.premium;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import school.faang.user_service.dto.premium.PremiumBoughtEvent;
import school.faang.user_service.publisher.EventPublisherAbstract;

@Component
public class PremiumBoughtEventPublisher extends EventPublisherAbstract<PremiumBoughtEvent> {

    @Value("${spring.data.redis.channels.premium-bought-channel}")
    private String premiumBoughtEventTopic;

    public PremiumBoughtEventPublisher(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        super(redisTemplate, objectMapper);
    }

    public void publish(PremiumBoughtEvent premiumBoughtEvent) {
        handleEvent(premiumBoughtEvent, premiumBoughtEventTopic);
    }
}
