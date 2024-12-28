package school.faang.user_service.service.subscription;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.prokectfilower.ProjectFollowerEvent;
import school.faang.user_service.publisher.projectfolllower.ProjectFollowerEventPublisher;
import school.faang.user_service.repository.ProjectSubscriptionRepository;

@Service
@Slf4j
@AllArgsConstructor
public class ProjectFollowerService {
    private final ProjectFollowerEventPublisher projectFollowerEventPublisher;
    private final ProjectSubscriptionRepository projectRepository;

    public void subscribeToProject(Long userId, Long projectId) {
        log.info("Subscribing user {} to project {}", userId, projectId);

        Long creatorId = projectRepository.findFollowerIdByProjectId(projectId).orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + projectId));
        // TODO реализовать метод подписки на проект
        projectFollowerEventPublisher.publish(new ProjectFollowerEvent(projectId,userId, creatorId));
    }
}
