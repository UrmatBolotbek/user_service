package school.faang.user_service.service.subscription;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProjectFollowerService {

    public void subscribeToProject(Long userId, Long projectId) {
        log.info("Subscribing user {} to project {}", userId, projectId);
        // TODO реализовать метод подписки на проект
    }
}
