package ru.practicum.evm.event.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evm.event.dto.LongEventDto;
import ru.practicum.evm.event.enums.SortValue;
import ru.practicum.evm.event.model.SearchEventParams;
import ru.practicum.evm.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/events")
public class EventPublicController {
    private final EventService eventService;

    @GetMapping("/{id}")
    public LongEventDto getEvent(@PathVariable Long id, HttpServletRequest request) {
        return eventService.getEvent(id, request);
    }

    @GetMapping
    public List<LongEventDto> getEventsWithParamsByUser(@RequestParam(required = false, defaultValue = "10") Integer size,
                                                        @RequestParam(required = false, defaultValue = "0") Integer from,
                                                        @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                                        @RequestParam(required = false) List<Long> categories,
                                                        @RequestParam(required = false) String rangeStart,
                                                        @RequestParam(required = false) String rangeEnd,
                                                        @RequestParam(required = false) SortValue sort,
                                                        @RequestParam(required = false) Boolean paid,
                                                        @RequestParam(required = false) String text,
                                                        HttpServletRequest request) {
        SearchEventParams eventParams = SearchEventParams.builder()
                .size(size)
                .from(from)
                .categories(categories)
                .onlyAvailable(onlyAvailable)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .sort(sort)
                .paid(paid)
                .text(text)
                .build();
        return eventService.getEventsWithParamsByUser(eventParams, request);
    }
}
