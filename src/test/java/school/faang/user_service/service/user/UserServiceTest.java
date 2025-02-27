package school.faang.user_service.service.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import school.faang.user_service.dto.user.ProfilePicEvent;
import school.faang.user_service.config.context.UserContext;
import school.faang.user_service.dto.user.SearchAppearanceEvent;
import school.faang.user_service.dto.user.UserDto;
import school.faang.user_service.dto.user.UserFilterDto;
import school.faang.user_service.entity.Country;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.UserProfilePic;
import school.faang.user_service.mapper.user.UserMapperImpl;
import school.faang.user_service.publisher.profile_pic.ProfilePicEventPublisher;
import school.faang.user_service.publisher.user.SearchAppearanceEventPublisher;
import school.faang.user_service.repository.CountryRepository;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.event.EventRepository;
import school.faang.user_service.repository.goal.GoalRepository;
import school.faang.user_service.service.mentorship.MentorshipService;
import school.faang.user_service.service.s3.S3Service;
import school.faang.user_service.service.user.filter.UserFilter;
import school.faang.user_service.service.user.random_password.PasswordGenerator;
import school.faang.user_service.validator.user.UserValidator;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private MentorshipService mentorshipService;

    @Spy
    private UserMapperImpl userMapper;

    @Mock
    private SearchAppearanceEventPublisher searchAppearanceEventPublisher;

    @Mock
    private UserContext userContext;

    @Mock
    private UserValidator userValidator;

    @Mock
    private S3Service s3Service;

    @Mock
    private ProfilePicEventPublisher eventPublisher;

    @Mock
    private PasswordGenerator passwordGenerator;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private List<UserFilter> userFilters;

    private final long userId = 1L;
    private User user;

    @BeforeEach
    public void initUser() {
        user = new User();
        user.setId(userId);
    }

    @Test
    public void testCreateUser() {
        String acceptLanguage = "en_US";
        when(userMapper.toUser(new UserDto())).thenReturn(user);

        userService.createUser(new UserDto(), acceptLanguage);

        Locale locale = Locale.forLanguageTag(acceptLanguage);
        user.setLocale(locale.toLanguageTag());

        assertNotNull(locale);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeactivateUser_Success() {
        user.setActive(true);

        when(userValidator.validateUser(userId)).thenReturn(user);

        userService.deactivateUser(userId);

        verify(goalRepository, times(1)).findGoalsByUserId(userId);
        verify(eventRepository, times(1)).findAllByUserId(userId);
        verify(mentorshipService, times(1)).stopMentorship(user);
        verify(userRepository, times(1)).save(user);

        assertFalse(user.isActive(), "User should be deactivated");
    }

    @Test
    public void testGetUserSuccess() {
        UserDto userDto = new UserDto();
        userDto.setId(userId);

        when(userValidator.validateUser(userId)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.getUser(userId);

        assertEquals(userDto, result);
        verify(userValidator, times(1)).validateUser(userId);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void testGetUserWithFiltersAndRedis() {
        UserFilterDto filterDto = new UserFilterDto();
        long searchingUserId = 2L;

        when(userContext.getUserId()).thenReturn(searchingUserId);
        when(userRepository.findAll()).thenReturn(List.of(user));

        UserFilter mockFilter = mock(UserFilter.class);
        when(mockFilter.isApplicable(filterDto)).thenReturn(true);
        when(mockFilter.apply(any(Stream.class), eq(filterDto)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(userFilters.iterator()).thenReturn(List.of(mockFilter).iterator());
        when(userRepository.findAllById(List.of(1L))).thenReturn(List.of(user));

        Stream<UserDto> result = userService.getUser(filterDto);

        ArgumentCaptor<SearchAppearanceEvent> captor = ArgumentCaptor.forClass(SearchAppearanceEvent.class);
        verify(searchAppearanceEventPublisher).publish(captor.capture());

        SearchAppearanceEvent capturedEvent = captor.getValue();
        assertEquals(List.of(1L), capturedEvent.getUserIds());
        assertEquals(searchingUserId, capturedEvent.getSearchingUserId());
        assertNotNull(capturedEvent.getViewedAt());

        List<UserDto> resultList = result.toList();
        assertEquals(1, resultList.size());
        assertEquals(user.getId(), resultList.get(0).getId());
    }

    @Test
    public void testGetUsersByIdsSuccess() {
        List<Long> ids = List.of(1L, 2L);
        List<User> users = List.of(new User(), new User());
        List<UserDto> usersDtos = List.of(new UserDto(), new UserDto());

        when(userRepository.findAllById(ids)).thenReturn(users);
        when(userMapper.toListDto(users)).thenReturn(usersDtos);

        List<UserDto> result = userService.getUsersByIds(ids);

        assertEquals(usersDtos, result);
        verify(userRepository, times(1)).findAllById(ids);
        verify(userMapper, times(2)).toListDto(users);
    }

    @Test
    public void testAddAvatar() {
        byte[] content = new byte[1024];
        MockMultipartFile file = new MockMultipartFile("file",content);
        user.setUserProfilePic(new UserProfilePic());
        user.getUserProfilePic().setFileId("123");

        when(userValidator.validateUser(userId)).thenReturn(user);

        userService.addAvatar(userId, file);

        verify(s3Service, times(1)).uploadFile(file, user);
        verify(userRepository, times(1)).save(user);
        ProfilePicEvent picEvent = new ProfilePicEvent(userId, user.getUserProfilePic().getFileId());
        verify(eventPublisher).publish(picEvent);
    }

    @Test
    public void testGetAvatarWhenUserPicProfileIsNotNull() {
        UserProfilePic profilePic = new UserProfilePic();
        user.setUserProfilePic(profilePic);
        profilePic.setFileId("123");

        user.setUserProfilePic(profilePic);

        when(userValidator.validateUser(userId))
                .thenReturn(user);
        when(s3Service.getFile(user.getUserProfilePic().getFileId()))
                .thenReturn(new ByteArrayInputStream("imageData".getBytes()));

        byte[] fileBytes = userService.getAvatar(userId);

        assertNotNull(fileBytes);
        Assertions.assertArrayEquals("imageData".getBytes(), fileBytes);
    }

    @Test
    public void testLoadingUsersViaFileSuccess() {
        String csvContent = String.join(System.lineSeparator(),
                "firstName,lastName,yearOfBirth,country,yearOfStudy",
                "John,Doe,1998,USA,2021",
                "Michael,Johnson,1988,USA,2021"
        );
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes()
        );
        when(countryRepository.findByTitleIgnoreCase("USA")).thenReturn(Optional.empty());
        when(passwordGenerator.generatePassword(15, true,
                true,true,true)).thenReturn("ksdfklsklfkslklfds");
        try {
            userService.loadingUsersViaFile(file);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        verify(userRepository, times(2)).save(any(User.class));
        verify(countryRepository, times(2)).save(any(Country.class));
    }
}