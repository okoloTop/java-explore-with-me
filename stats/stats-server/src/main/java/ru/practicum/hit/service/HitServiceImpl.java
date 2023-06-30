package ru.practicum.hit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.hit.mapper.HitMapper;
import ru.practicum.hit.model.Hit;
import ru.practicum.hit.repository.StatsRepository;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class HitServiceImpl implements HitService {
    private final StatsRepository statsRepository;

    @Transactional
    @Override
    public HitDto create(HitDto hitDto) {
        Hit hit = statsRepository.save(HitMapper.mapToHit(hitDto));
        return HitMapper.mapToEndpointHit(hit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ViewStatsDto> findStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (unique) {
            return statsRepository.calculateUniqueStats(uris, start, end);
        } else if (uris == null) {
            return statsRepository.calculateStats(start, end);
        } else {
            return statsRepository.calculateStatsWithUri(uris, start, end);
        }
    }
}
