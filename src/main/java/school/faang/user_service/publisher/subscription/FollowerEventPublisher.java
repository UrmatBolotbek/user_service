package school.faang.user_service.publisher.subscription;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import school.faang.user_service.dto.subscription.FollowerEvent;
import school.faang.user_service.publisher.EventPublisherAbstract;

@Slf4j
@Component
public class FollowerEventPublisher extends EventPublisherAbstract<FollowerEvent> {

    @Value("${spring.data.redis.channels.follower-event-channel}")
    private String topicFollower;

    public FollowerEventPublisher(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        super(redisTemplate, objectMapper);
    }

    public void publish(FollowerEvent event) {
        handleEvent(event, topicFollower);
    }
}
