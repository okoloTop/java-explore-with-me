package ru.practicum.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.hit.model.Hit;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query("SELECT new ru.practicum.stats.dto.ViewStatsDto(app, uri, COUNT(DISTINCT ip)) " +
            "FROM Hit " +
            "WHERE uri IN :uris AND (timestamp >= :start AND timestamp < :end) GROUP BY (app, uri)" +
            "ORDER BY COUNT(uri) DESC"
    )
    List<ViewStatsDto> calculateUniqueStats(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.stats.dto.ViewStatsDto(app, uri, COUNT(ip)) " +
            "FROM Hit " +
            "WHERE uri IN :uris AND (timestamp >= :start AND timestamp <= :end) GROUP BY (app, uri)" +
            "ORDER BY COUNT(uri) DESC"
    )
    List<ViewStatsDto> calculateStatsWithUri(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.stats.dto.ViewStatsDto(app, uri, COUNT(ip))" +
            "FROM Hit " +
            "GROUP BY (app, uri)" +
            "ORDER BY COUNT(uri) DESC"
    )
    List<ViewStatsDto> calculateStats(LocalDateTime start, LocalDateTime end);

}