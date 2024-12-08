package school.faang.user_service.publisher.subscription;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import school.faang.user_service.dto.FollowerEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class FollowerEventPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.data.redis.channel.follower}")
    private String followerTopicName;

    public void publish(FollowerEvent followerEvent) {
        try {
            String json = objectMapper.writeValueAsString(followerEvent);
            redisTemplate.convertAndSend(followerTopicName, json);
        } catch (JsonProcessingException e) {
            log.error("An error occurred while working with JSON: ", e);
            throw new RuntimeException(e);
        }
    }
}
