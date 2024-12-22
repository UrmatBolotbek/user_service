package school.faang.user_service.controller.subscription;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import school.faang.user_service.config.context.UserContext;
import school.faang.user_service.service.subscription.ProjectFollowerService;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProjectFollowerController.class)
public class ProjectFollowerControllerTest {
    @InjectMocks
    private ProjectFollowerController projectFollowerController;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectFollowerService projectFollowerService;

    @MockBean
    private UserContext userContext;

    @BeforeEach
    public void setup() {
        when(userContext.getUserId()).thenReturn(1L);
    }

    @Test
    public void testSubscribeToProject_Success() throws Exception {
        // Мокаем возвращаемый userId
        when(userContext.getUserId()).thenReturn(123L);

        // Выполняем запрос и проверяем, что статус ответа 200
        mockMvc.perform(post("/api/v1/subscribe/project/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Проверяем вызов сервиса с правильными параметрами
        verify(projectFollowerService, times(1)).subscribeToProject(123L, 1L);
    }

    @Test
    public void testSubscribeToProject_Failure() throws Exception {
        // Мокаем выброс исключения в сервисе
        when(userContext.getUserId()).thenReturn(123L);
        doThrow(new RuntimeException("Test exception")).when(projectFollowerService).subscribeToProject(anyLong(), anyLong());

        // Выполняем запрос и проверяем, что статус ответа 500 и правильные значения в JSON
        mockMvc.perform(post("/api/v1/subscribe/project/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        // Проверяем вызов сервиса
        verify(projectFollowerService, times(1)).subscribeToProject(123L, 1L);
    }
}