package ru.practicum.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.hit.model.Hit;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query(value = ""
            + "SELECT new ru.practicum.stats.dto.ViewStatsDto(hit.uri, hit.app, COUNT(DISTINCT hit.ip)) "
            + "FROM Hit hit "
            + "WHERE hit.uri IN ?3 AND hit.timestamp BETWEEN ?1 AND ?2 "
            + "GROUP BY hit.uri, hit.app "
            + "ORDER BY COUNT(DISTINCT hit.ip) DESC")
    List<ViewStatsDto> calculateUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = ""
            + "SELECT new ru.practicum.stats.dto.ViewStatsDto(hit.uri, hit.app, COUNT(hit.ip)) "
            + "FROM Hit hit "
            + "WHERE hit.uri IN ?3 AND hit.timestamp BETWEEN ?1 AND ?2 "
            + "GROUP BY hit.uri, hit.app "
            + "ORDER BY COUNT(hit.ip) DESC")
    List<ViewStatsDto> calculateStatsWithUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = ""
            + "SELECT new ru.practicum.stats.dto.ViewStatsDto(hit.uri, hit.app, COUNT(hit.ip)) "
            + "FROM Hit hit "
            + "WHERE hit.timestamp BETWEEN ?1 AND ?2 "
            + "GROUP BY hit.uri, hit.app "
            + "ORDER BY COUNT(hit.ip) DESC")
    List<ViewStatsDto> calculateStats(LocalDateTime start, LocalDateTime end, List<String> uris);
}