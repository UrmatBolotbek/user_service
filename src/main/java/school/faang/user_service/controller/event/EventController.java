package school.faang.user_service.controller.event;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school.faang.user_service.dto.event.EventDto;
import school.faang.user_service.dto.event.EventFilterDto;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.service.event.EventService;

import java.util.List;

@RequestMapping("api/v1/events")
@RestController
@RequiredArgsConstructor
@Tag(name = "Event Controller", description = "Контроллер для управления событиями")
@ApiResponse(description = "Успешная обработка метода", responseCode = "200")
@ApiResponse(description = "Ошибка клиента", responseCode = "400")
@ApiResponse(description = "Ошибка сервера", responseCode = "500")
public class EventController {

    private final EventService eventService;

    @Operation(
            summary = "Создание события с помощью eventDto"
    )
    @PostMapping()
    public EventDto create(@Valid @RequestBody EventDto event) {
        validateByTitleOwnerIdStartDate(event);
        return eventService.create(event);
    }

    @Operation(
            summary = "Получение события по id"
    )
    @GetMapping()
    public EventDto get(@RequestParam long eventId) {
        return eventService.get(eventId);
    }

    @Operation(
            summary = "Получение листа отфильтрованных событий",
            description = "Возможные фильтры: " +
                    "временной промежуток, " +
                    "локация, максимальное " +
                    "количество участников, " +
                    "имя владельца события, " +
                    "навыки, необходимые для участия в событии, " +
                    "название события"
    )
    @GetMapping("/filter")
    public List<EventDto> getByFilter(@Valid @ModelAttribute EventFilterDto filter) {
        return eventService.getByFilter(filter);
    }

    @Operation(
            summary = "Удаление события по id"
    )
    @DeleteMapping("/{eventId}")
    public void delete(@PathVariable long eventId) {
        eventService.delete(eventId);
    }

    @Operation(
            summary = "Обновление события через EventDto"
    )
    @PutMapping()
    public EventDto update(@Valid @RequestBody EventDto event) {
        validateByTitleOwnerIdStartDate(event);
        return eventService.update(event);
    }

    @Operation(
            summary = "Получение всех событий принадлежащих пользователю по его id"
    )
    @GetMapping("/owner/{ownerId}")
    public List<EventDto> getOwnedEvents(@PathVariable long ownerId) {
        return eventService.getOwnedEvents(ownerId);
    }

    @Operation(
            summary = "Получение всех событий ы которых пользователь принимает участие по его id"
    )
    @GetMapping("/participant/{participantId}")
    public List<EventDto> getParticipatedEvents(@PathVariable long participantId) {
        return eventService.getParticipatedEvents(participantId);
    }

    private void validateByTitleOwnerIdStartDate(EventDto event) {
        if (event.getTitle() == null || event.getTitle().trim().isEmpty()) {
            throw new DataValidationException("Event name is required!");
        }
        if (event.getOwnerId() == null) {
            throw new DataValidationException("Event owner id is required");
        }
        if (event.getStartDate() == null) {
            throw new DataValidationException("Event start date is required");
        }
    }
}