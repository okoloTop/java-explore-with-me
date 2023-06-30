package ru.practicum.hit.mapper;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.hit.model.Hit;
import ru.practicum.stats.dto.HitDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HitMapper {
    public static HitDto mapToEndpointHit(Hit hit) {
        return new HitDto(
                hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp()
        );
    }

    public static Hit mapToHit(HitDto endpointHit) {
        return new Hit(
                null,
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                endpointHit.getTimestamp()
        );
    }
}

