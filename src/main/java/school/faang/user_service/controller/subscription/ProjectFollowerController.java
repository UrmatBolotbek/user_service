package school.faang.user_service.controller.subscription;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import school.faang.user_service.config.context.UserContext;
import school.faang.user_service.service.subscription.ProjectFollowerService;
@Controller
@AllArgsConstructor
@Slf4j
public class ProjectFollowerController {
    private final ProjectFollowerService projectFollowerService;
    private final UserContext userContext;
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String>  subscribeToProject(
            @PathVariable("projectId") Long projectId){
        Long userId = userContext.getUserId();
        log.info("User {} is subscribing to project {}", userId, projectId);
        projectFollowerService.subscribeToProject(userId, projectId);
        return ResponseEntity.ok("Subscription successful");
    }
}
