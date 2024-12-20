package school.faang.user_service.service.subscription;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.prokectfilower.ProjectFollowerEvent;
import school.faang.user_service.publisher.projectfolllower.ProjectFollowerEventPublisher;

@Service
@Slf4j
@AllArgsConstructor
public class ProjectFollowerService {
    private final ProjectFollowerEventPublisher projectFollowerEventPublisher;

    public void subscribeToProject(Long userId, Long projectId, long creatorId) {
        log.info("Subscribing user {} to project {}", userId, projectId);
        // TODO реализовать метод подписки на проект
        projectFollowerEventPublisher.publish(new ProjectFollowerEvent(projectId,userId, creatorId));
    }
}
