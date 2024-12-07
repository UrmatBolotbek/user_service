package school.faang.user_service.publisher.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import school.faang.user_service.dto.event.EventStartReminderEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventStartReminderEventPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.data.redis.channels.event-start-reminder-event-channel.name}")
    private String eventStartReminderEventTopic;

    public void publish(EventStartReminderEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend(eventStartReminderEventTopic, json);
        } catch (JsonProcessingException e) {
            log.error("Error serializing EventStartReminderEvent to JSON", e);
            throw new RuntimeException(e);
        }
    }
}
