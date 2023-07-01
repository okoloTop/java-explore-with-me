package ru.practicum.hit.mapper;

import ru.practicum.stats.dto.ViewStatsDto;

public class ViewStatsMapper {
    public static ViewStatsDto toViewStatsDto(ViewStatsDto viewStats) {
        return ViewStatsDto.builder()
                .hits(viewStats.getHits())
                .uri(viewStats.getUri())
                .app(viewStats.getApp())
                .build();
    }
}
