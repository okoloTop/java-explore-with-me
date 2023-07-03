package ru.practicum.hit.mapper;

import ru.practicum.hit.model.ViewStats;
import ru.practicum.stats.dto.ViewStatsDto;

public class ViewStatsMapper {
    public static ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        return ViewStatsDto.builder()
                .hits(viewStats.getHits())
                .uri(viewStats.getUri())
                .app(viewStats.getApp())
                .build();
    }
}
