package school.faang.user_service.controller.subscription;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import school.faang.user_service.config.context.UserContext;
import school.faang.user_service.service.subscription.ProjectFollowerService;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectFollowerController.class)

public class ProjectFollowerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProjectFollowerService projectFollowerService;

    @MockBean
    private UserContext userContext;


    @Test
    void testSubscribeToProject_Success() throws Exception {
        Long userId = 1L;
        Long projectId = 123L;
        Long creatorId = 456L;

        Mockito.when(userContext.getUserId()).thenReturn(userId);

        mockMvc.perform(post("/api/v1/subscribe/project/{projectId}?creatorId={creatorId}", projectId, creatorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Subscription successful"));

        Mockito.verify(projectFollowerService, times(1))
                .subscribeToProject(userId, projectId, creatorId);
    }

    @Test
    public void testSubscribeToProject_Failure() throws Exception {
        mockMvc.perform(post("/api/v1/subscribe/project/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 123}"))
                .andExpect(status().isInternalServerError());
    }
}
