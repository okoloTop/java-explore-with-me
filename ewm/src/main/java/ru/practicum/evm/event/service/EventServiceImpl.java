package ru.practicum.evm.event.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.evm.category.exception.CategoryNotExistException;
import ru.practicum.evm.category.repository.CategoryRepository;
import ru.practicum.evm.event.dto.*;
import ru.practicum.evm.event.exception.EventCanceledException;
import ru.practicum.evm.event.exception.EventNotExistException;
import ru.practicum.evm.event.exception.EventPublishedException;
import ru.practicum.evm.event.exception.EventWrongTimeException;
import ru.practicum.evm.event.mapper.EventMapper;
import ru.practicum.evm.event.model.Event;
import ru.practicum.evm.event.model.SearchEventParams;
import ru.practicum.evm.event.repository.EventRepository;
import ru.practicum.evm.user.exception.UserNotExistException;
import ru.practicum.evm.user.repository.UserRepository;
import ru.practicum.evm.utils.Patterns;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.PageRequest.of;
import static ru.practicum.evm.event.enums.EventState.*;
import static ru.practicum.evm.event.enums.SortValue.EVENT_DATE;
import static ru.practicum.evm.event.enums.StateActionForAdmin.PUBLISH_EVENT;
import static ru.practicum.evm.event.enums.StateActionForAdmin.REJECT_EVENT;
import static ru.practicum.evm.event.enums.StateActionForUser.SEND_TO_REVIEW;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;

    @Override
    @Transactional
    public LongEventDto saveEvent(Long userId,
                                  NewEventDto newEventDto) {
        var category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(
                () -> new CategoryNotExistException("Нет такой категории"));
        var eventDate = newEventDto.getEventDate();

        if (eventDate.isBefore(now().plusHours(2)))
            throw new EventWrongTimeException("Дата события должна быть в будущем");

        var event = eventMapper.toEvent(newEventDto);
        event.setCategory(category);

        if (newEventDto.getRequestModeration() == null)
            event.setRequestModeration(true);

        var user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotExistException("Пользователь#" + userId + " не существует"));

        event.setInitiator(user);

        return eventMapper.toLongEventDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public LongEventDto updateEvent(Long eventId,
                                    UpdateEventAdminDto updateEventAdminDto) {
        var event = eventRepository.findById(eventId).orElseThrow(
                () -> new EventNotExistException("Событие#" + eventId + " не существует"));

        if (updateEventAdminDto == null)
            return eventMapper.toLongEventDto(event);

        if (updateEventAdminDto.getAnnotation() != null)
            event.setAnnotation(updateEventAdminDto.getAnnotation());

        if (updateEventAdminDto.getCategory() != null) {
            var category = categoryRepository.findById(updateEventAdminDto.getCategory()).orElseThrow(
                    () -> new CategoryNotExistException("Нет такой категории"));
            event.setCategory(category);
        }
        if (updateEventAdminDto.getDescription() != null)
            event.setDescription(updateEventAdminDto.getDescription());

        if (updateEventAdminDto.getLocation() != null)
            event.setLocation(updateEventAdminDto.getLocation());

        if (updateEventAdminDto.getPaid() != null)
            event.setPaid(updateEventAdminDto.getPaid());

        if (updateEventAdminDto.getParticipantLimit() != null)
            event.setParticipantLimit(updateEventAdminDto.getParticipantLimit().intValue());

        if (updateEventAdminDto.getRequestModeration() != null)
            event.setRequestModeration(updateEventAdminDto.getRequestModeration());

        if (updateEventAdminDto.getTitle() != null)
            event.setTitle(updateEventAdminDto.getTitle());

        if (updateEventAdminDto.getStateAction() != null) {
            if (PUBLISH_EVENT.equals(updateEventAdminDto.getStateAction())) {
                if (event.getPublishedOn() != null)
                    throw new EventPublishedException("Событие уже опубликовано");
                if (CANCELED.equals(event.getState()))
                    throw new EventCanceledException("Событие отменено");
                event.setState(PUBLISHED);
                event.setPublishedOn(now());
            } else if (REJECT_EVENT.equals(updateEventAdminDto.getStateAction())) {
                if (event.getPublishedOn() != null)
                    throw new EventPublishedException("Событие уже опубликовано ");
                event.setState(CANCELED);
            }
        }
        if (updateEventAdminDto.getEventDate() != null) {
            var eventTime = updateEventAdminDto.getEventDate();
            if (eventTime.isBefore(now())
                    || event.getPublishedOn() != null && eventTime.isBefore(event.getPublishedOn().plusHours(1)))
                throw new EventWrongTimeException("Неверное время");

            event.setEventDate(updateEventAdminDto.getEventDate());
        }
        var saved = eventRepository.save(event);
        System.out.println(saved);

        return eventMapper.toLongEventDto(saved);
    }

    @Override
    @Transactional
    public LongEventDto updateEventByUser(Long userId,
                                          Long eventId,
                                          UpdateEventUserDto updateEventUserDto) {
        var event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(
                () -> new EventNotExistException("Событие#" + eventId + " не существует"));

        if (event.getPublishedOn() != null)
            throw new EventPublishedException("Событие уже опубликовано");

        if (updateEventUserDto == null)
            return eventMapper.toLongEventDto(event);

        if (updateEventUserDto.getAnnotation() != null)
            event.setAnnotation(updateEventUserDto.getAnnotation());

        if (updateEventUserDto.getCategory() != null) {
            var category = categoryRepository.findById(updateEventUserDto.getCategory()).orElseThrow(
                    () -> new CategoryNotExistException("Категория не существует"));
            event.setCategory(category);
        }
        if (updateEventUserDto.getDescription() != null)
            event.setDescription(updateEventUserDto.getDescription());

        if (updateEventUserDto.getEventDate() != null) {
            var eventTime = updateEventUserDto.getEventDate();
            if (eventTime.isBefore(now().plusHours(2)))
                throw new EventWrongTimeException("Неверное время");
            event.setEventDate(updateEventUserDto.getEventDate());
        }
        if (updateEventUserDto.getLocation() != null)
            event.setLocation(updateEventUserDto.getLocation());

        if (updateEventUserDto.getPaid() != null)
            event.setPaid(updateEventUserDto.getPaid());

        if (updateEventUserDto.getParticipantLimit() != null)
            event.setParticipantLimit(updateEventUserDto.getParticipantLimit().intValue());

        if (updateEventUserDto.getRequestModeration() != null)
            event.setRequestModeration(updateEventUserDto.getRequestModeration());

        if (updateEventUserDto.getTitle() != null)
            event.setTitle(updateEventUserDto.getTitle());

        if (updateEventUserDto.getStateAction() != null) {
            if (SEND_TO_REVIEW.equals(updateEventUserDto.getStateAction()))
                event.setState(PENDING);
            else
                event.setState(CANCELED);
        }
        return eventMapper.toLongEventDto(eventRepository.save(event));
    }

    @Override
    public List<ShortEventDto> getEvents(Long userId,
                                         Integer from,
                                         Integer size) {
        var events = eventRepository.findAllByInitiatorId(userId, of(from / size, size)).toList();
        return eventMapper.toShortEventDtos(events);
    }

    @Override
    public LongEventDto getEventByUser(Long userId,
                                       Long eventId) {
        return eventMapper.toLongEventDto(eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(
                () -> new EventNotExistException("Событие#" + eventId + " не существует")));
    }

    @Override
    public LongEventDto getEvent(Long id,
                                 HttpServletRequest request) {
        var event = eventRepository.findByIdAndPublishedOnIsNotNull(id).orElseThrow(
                () -> new EventNotExistException("Событие#" + id + " не существует"));
        addView(event);
        sendStats(event, request);
        return eventMapper.toLongEventDto(event);
    }

    @Override
    public List<LongEventDto> getEventsWithParamsByAdmin(SearchEventParams eventParams) {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(Event.class);
        var root = query.from(Event.class);
        var criteria = builder.conjunction();

        var start = eventParams.getRangeStart() == null ? null : parse(eventParams.getRangeStart(), ofPattern(Patterns.DATE_PATTERN));
        var end = eventParams.getRangeEnd() == null ? null : parse(eventParams.getRangeEnd(), ofPattern(Patterns.DATE_PATTERN));

        if (eventParams.getRangeStart() != null)
            criteria = builder.and(criteria, builder.greaterThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), start));

        if (eventParams.getRangeEnd() != null)
            criteria = builder.and(criteria, builder.lessThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), end));

        if (eventParams.getCategories() != null && eventParams.getCategories().size() > 0)
            criteria = builder.and(criteria, root.get("category").in(eventParams.getCategories()));

        if (eventParams.getUsers() != null && eventParams.getUsers().size() > 0)
            criteria = builder.and(criteria, root.get("initiator").in(eventParams.getUsers()));

        if (eventParams.getStates() != null)
            criteria = builder.and(criteria, root.get("state").in(eventParams.getStates()));

        query.select(root).where(criteria);

        var events = entityManager.createQuery(query)
                .setFirstResult(eventParams.getFrom())
                .setMaxResults(eventParams.getSize())
                .getResultList();

        if (events.size() == 0) return new ArrayList<>();

        return eventMapper.toLongEventDtos(events);
    }

    @Override
    public List<LongEventDto> getEventsWithParamsByUser(SearchEventParams eventParams,
                                                        HttpServletRequest request) {

        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(Event.class);
        var root = query.from(Event.class);
        var criteria = builder.conjunction();


        var start = eventParams.getRangeStart() == null ? null : parse(eventParams.getRangeStart(), ofPattern(Patterns.DATE_PATTERN));
        var end = eventParams.getRangeEnd() == null ? null : parse(eventParams.getRangeEnd(), ofPattern(Patterns.DATE_PATTERN));
        if (start != null && end != null) {
            checkEventDate(start, end);
        }
        if (eventParams.getText() != null) {
            criteria = builder.and(criteria, builder.or(
                    builder.like(
                            builder.lower(root.get("annotation")), "%" + eventParams.getText().toLowerCase() + "%"),
                    builder.like(
                            builder.lower(root.get("description")), "%" + eventParams.getText().toLowerCase() + "%")));
        }

        if (eventParams.getCategories() != null && eventParams.getCategories().size() > 0)
            criteria = builder.and(criteria, root.get("category").in(eventParams.getCategories()));

        if (eventParams.getPaid() != null) {
            Predicate predicate;
            if (eventParams.getPaid()) predicate = builder.isTrue(root.get("paid"));
            else predicate = builder.isFalse(root.get("paid"));
            criteria = builder.and(criteria, predicate);
        }

        if (eventParams.getRangeEnd() != null)
            criteria = builder.and(criteria, builder.lessThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), end));

        if (eventParams.getRangeStart() != null)
            criteria = builder.and(criteria, builder.greaterThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), start));

        query.select(root).where(criteria).orderBy(builder.asc(root.get("eventDate")));

        var events = entityManager.createQuery(query)
                .setFirstResult(eventParams.getFrom())
                .setMaxResults(eventParams.getSize())
                .getResultList();

        if (eventParams.getOnlyAvailable())
            events = events.stream()
                    .filter((event -> event.getConfirmedRequests() < (long) event.getParticipantLimit()))
                    .collect(toList());

        if (eventParams.getSort() != null) {
            if (EVENT_DATE.equals(eventParams.getSort()))
                events = events.stream()
                        .sorted(comparing(Event::getEventDate))
                        .collect(toList());
            else
                events = events.stream()
                        .sorted(comparing(Event::getViews))
                        .collect(toList());
        }
        if (events.size() == 0) return new ArrayList<>();

        sendStats(events, request);
        return eventMapper.toLongEventDtos(events);
    }

    private List<ViewStatsDto> getStats(String start,
                                        String end,
                                        String uri) {
        return statsClient.findStats(start, end, uri, true);
    }

    private void addView(Event event) {
        var start = event.getCreatedOn().format(ofPattern(Patterns.DATE_PATTERN));
        var end = now().format(ofPattern(Patterns.DATE_PATTERN));
        var uris = "/events/" + event.getId();
        var stats = getStats(start, end, uris);

        if (stats.size() == 1)
            event.setViews(stats.get(0).getHits());
        else
            event.setViews(0L);
    }

    private void sendStats(Event event,
                           HttpServletRequest request) {
        var now = now();
        var requestDto = HitDto.builder()
                .ip(request.getRemoteAddr())
                .app("main")
                .uri("/events")
                .timestamp(now)
                .build();

        statsClient.create(requestDto);
        sendStatsForTheEvent(
                event.getId(),
                request.getRemoteAddr(),
                now
        );
    }

    private void sendStats(List<Event> events,
                           HttpServletRequest request) {
        var now = now();
        var requestDto = HitDto.builder()
                .ip(request.getRemoteAddr())
                .app("main")
                .uri("/events")
                .timestamp(now)
                .build();

        statsClient.create(requestDto);
        sendStatsForEveryEvent(
                events,
                request.getRemoteAddr(),
                now()
        );
    }

    private void sendStatsForTheEvent(Long eventId,
                                      String remoteAddress,
                                      LocalDateTime now) {
        var requestDto = HitDto.builder()
                .ip(remoteAddress)
                .app("main")
                .uri("/events/" + eventId)
                .timestamp(now)
                .build();

        statsClient.create(requestDto);
    }

    private void sendStatsForEveryEvent(List<Event> events,
                                        String remoteAddress,
                                        LocalDateTime now) {
        for (var event : events) {
            var requestDto = HitDto.builder()
                    .ip(remoteAddress)
                    .app("main")
                    .uri("/events/" + event.getId())
                    .timestamp(now)
                    .build();

            statsClient.create(requestDto);
        }
    }

    private void checkEventDate(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new EventWrongTimeException("Неверные даты");
        }
    }
}
