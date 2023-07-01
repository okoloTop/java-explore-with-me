package ru.practicum.hit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.hit.mapper.HitMapper;
import ru.practicum.hit.mapper.ViewStatsMapper;
import ru.practicum.hit.model.Hit;
import ru.practicum.hit.repository.StatsRepository;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

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

    @Override
    public List<ViewStatsDto> findStats(LocalDateTime start,
                                        LocalDateTime end,
                                        List<String> uris,
                                        boolean unique) {
        List<ViewStatsDto> hits;

        if (unique)
            hits = statsRepository.calculateUniqueStats(start, end, uris);
        else if (uris != null)
            hits = statsRepository.calculateStatsWithUri(start, end, uris);
        else
            hits = statsRepository.calculateStats(start, end, uris);
        return hits.stream()
                .map(ViewStatsMapper::toViewStatsDto)
                .collect(toList());
    }
}
