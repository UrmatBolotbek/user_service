package school.faang.user_service.service.mentorship.request_filter;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import school.faang.user_service.dto.user.UserDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.mapper.user.UserMapper;
import school.faang.user_service.repository.event.EventParticipationRepository;
import school.faang.user_service.validator.event.EventValidator;

import java.util.List;


@Component
@RequiredArgsConstructor
public class EventParticipationService {

    private final EventParticipationRepository eventParticipationRepository;
    private final UserMapper userMapper;
    private final EventValidator eventValidator;

    @Transactional
    public void registerParticipant(long eventId, long userId) {
        eventValidator.checkUserExists(userId);
        eventValidator.validateEventExists(eventId);
        eventValidator.validateUserNotRegistered(eventId, userId);
        eventParticipationRepository.register(eventId, userId);
    }

    public void unregisterParticipant(long eventId, long userId) {
        eventValidator.checkUserExists(userId);
        eventValidator.validateEventExists(eventId);
        eventValidator.validateUserIsRegistered(eventId, userId);
        eventParticipationRepository.unregister(eventId, userId);
    }

    public List<UserDto> getParticipants(long eventId) {
        eventValidator.validateEventExists(eventId);

        List<User> users = eventParticipationRepository.findUsersByEventId(eventId);
        return userMapper.toListDto(users);
    }

    public int getParticipantsCount(long eventId) {
        eventValidator.validateEventExists(eventId);
        return eventParticipationRepository.countParticipants(eventId);
    }
}
