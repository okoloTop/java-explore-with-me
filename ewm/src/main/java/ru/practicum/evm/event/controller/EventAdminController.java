package ru.practicum.evm.event.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evm.event.dto.LongEventDto;
import ru.practicum.evm.event.dto.UpdateEventAdminDto;
import ru.practicum.evm.event.enums.EventState;
import ru.practicum.evm.event.model.SearchEventParams;
import ru.practicum.evm.event.service.EventService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class EventAdminController {
    private final EventService eventService;

    @GetMapping("/events")
    public List<LongEventDto> getEvents(@RequestParam(required = false, defaultValue = "10") Integer size,
                                        @RequestParam(required = false, defaultValue = "0") Integer from,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false) EventState states,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) List<Long> users,
                                        @RequestParam(required = false) String rangeEnd) {
        SearchEventParams eventParams = SearchEventParams.builder()
                .size(size)
                .from(from)
                .categories(categories)
                .states(states)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .users(users)
                .build();

        return eventService.getEventsWithParamsByAdmin(eventParams);
    }

    @PatchMapping("/events/{eventId}")
    public LongEventDto updateEvent(@Valid @RequestBody UpdateEventAdminDto updateEventAdminDto,
                                    @PathVariable Long eventId) {
        return eventService.updateEvent(eventId, updateEventAdminDto);
    }
}
