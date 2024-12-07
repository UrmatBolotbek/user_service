package school.faang.user_service.publisher.goal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import school.faang.user_service.dto.goal.GoalCompletedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoalCompletedEventPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.data.redis.channel.goal-completed-channel}")
    private String topicGoalCompleted;

    public void publish(GoalCompletedEvent event) {
        redisTemplate.convertAndSend(topicGoalCompleted, event);
    }


}
