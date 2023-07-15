package ru.practicum.evm.event.service;

import ru.practicum.evm.event.dto.*;
import ru.practicum.evm.event.model.SearchEventParams;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    LongEventDto saveEvent(Long userId,
                           NewEventDto newEventDto);

    LongEventDto updateEvent(Long eventId,
                             UpdateEventAdminDto updateEventAdminDto);

    LongEventDto updateEventByUser(Long userId,
                                   Long eventId,
                                   UpdateEventUserDto updateEventUserDto);

    List<ShortEventDto> getEvents(Long userId,
                                  Integer from,
                                  Integer size);

    LongEventDto getEventByUser(Long userId,
                                Long eventId);

    LongEventDto getEvent(Long id,
                          HttpServletRequest request);

    List<LongEventDto> getEventsWithParamsByAdmin(SearchEventParams eventParams);

    List<LongEventDto> getEventsWithParamsByUser(SearchEventParams eventParams,
                                                 HttpServletRequest request);
}
