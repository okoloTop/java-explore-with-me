package ru.practicum.hit.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.hit.service.HitService;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@AllArgsConstructor
public class HitController {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final HitService hitService;

    @PostMapping("/hit")
    @ResponseStatus(CREATED)
    public HitDto saveHit(@RequestBody @Valid HitDto hitDto) {
        return hitService.create(hitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam String start,
                                       @RequestParam String end,
                                       @RequestParam(defaultValue = "false") boolean unique,
                                       @RequestParam(required = false) List<String> uris) {
        return hitService.findStats(LocalDateTime.parse(start, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)),
                LocalDateTime.parse(end, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)), uris, unique);
    }
}
