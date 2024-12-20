package school.faang.user_service.listener.user_ban;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import school.faang.user_service.dto.user.UserForBanEventDto;
import school.faang.user_service.listener.AbstractEventListener;
import school.faang.user_service.service.user.UserService;

@Component
public class UserBanEventListener extends AbstractEventListener<UserForBanEventDto> implements MessageListener {

    @Value("${spring.data.redis.channels.ban-channel}")
    private String topicUserBan;

    private final UserService userService;

    public UserBanEventListener(ObjectMapper objectMapper,
                                RedisMessageListenerContainer container, UserService userService) {
        super(objectMapper, container);
        this.userService = userService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, UserForBanEventDto.class, userService::banUser);
    }

    @Override
    protected String getTopicName() {
        return topicUserBan;
    }

}
