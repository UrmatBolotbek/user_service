package school.faang.user_service.controller.subscription;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.faang.user_service.config.context.UserContext;
import school.faang.user_service.service.subscription.ProjectFollowerService;

@RestController
@RequestMapping("/api/v1/subscribe")
@AllArgsConstructor
@Slf4j
public class ProjectFollowerController {
    private final ProjectFollowerService projectFollowerService;
    private final UserContext userContext;

    @PostMapping("/project/{projectId}")
    public ResponseEntity<String> subscribeToProject(@PathVariable("projectId") Long projectId) {

        Long userId = userContext.getUserId();
        projectFollowerService.subscribeToProject(userId, projectId);
        return  ResponseEntity.ok().build();

    }
}