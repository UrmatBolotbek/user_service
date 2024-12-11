package school.faang.user_service.dto.prokectfilower;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectFollowerEvent {
    private Long subscriberId;
    private Long projectId;
    private Long creatorId;

}
