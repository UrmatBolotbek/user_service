package school.faang.user_service.publisher.projectfolllower;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import school.faang.user_service.dto.prokectfilower.ProjectFollowerEvent;
import school.faang.user_service.publisher.EventPublisherAbstract;

@Component
@Slf4j
public class ProjectFollowerEventPublisher extends EventPublisherAbstract<ProjectFollowerEvent> {
    @Value("${spring.data.redis.channels.project-follower-channel}")
    private String topicProjectFollower;

    public ProjectFollowerEventPublisher(RedisTemplate<String,
            Object> redisTemplate, ObjectMapper objectMapper) {
        super(redisTemplate, objectMapper);
    }

    public void publish(ProjectFollowerEvent event) {
        handleEvent(event, topicProjectFollower);
    }
}
