package ru.practicum.hit.service;

import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;


public interface HitService {
    HitDto create(HitDto HitDto);

    List<ViewStatsDto> findStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
