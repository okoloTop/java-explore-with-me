package ru.practicum.evm.compilation.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.evm.compilation.dto.CompilationDto;
import ru.practicum.evm.compilation.dto.CompilationUpdateRequest;
import ru.practicum.evm.compilation.dto.SavedCompilationDto;
import ru.practicum.evm.compilation.model.Compilation;
import ru.practicum.evm.compilation.exception.CompilationNotExistException;
import ru.practicum.evm.compilation.mapper.CompilationMapper;
import ru.practicum.evm.compilation.repository.CompilationRepository;
import ru.practicum.evm.event.repository.EventRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public CompilationDto saveCompilation(SavedCompilationDto savedCompilationDto) {
        if (savedCompilationDto.getEvents() != null) {
            var events = eventRepository.findAllByIdIn(savedCompilationDto.getEvents());

            var compilation = Compilation.builder()
                    .title(savedCompilationDto.getTitle())
                    .events(new HashSet<>(events))
                    .build();
            if (savedCompilationDto.getPinned() == null) {
                compilation.setPinned(false);
            } else {
                compilation.setPinned(savedCompilationDto.getPinned());
            }

            var saved = compilationRepository.save(compilation);
            return compilationMapper.mapToCompilationDto(saved);
        } else {
            var compilation = Compilation.builder()
                    .title(savedCompilationDto.getTitle())
                    .events(new HashSet<>())
                    .build();
            if (savedCompilationDto.getPinned() == null) {
                compilation.setPinned(false);
            } else {
                compilation.setPinned(savedCompilationDto.getPinned());
            }
            var saved = compilationRepository.save(compilation);
            return compilationMapper.mapToCompilationDto(saved);
        }
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId,
                                            CompilationUpdateRequest compilationUpdateRequest) {
        var old = compilationRepository.findById(compId).orElseThrow(
                () -> new CompilationNotExistException("Подборка не существует"));
        var eventsIds = compilationUpdateRequest.getEvents();
        if (eventsIds != null) {
            var events = eventRepository.findAllByIdIn(compilationUpdateRequest.getEvents());
            old.setEvents(new HashSet<>(events));
        }
        if (compilationUpdateRequest.getPinned() != null)
            old.setPinned(compilationUpdateRequest.getPinned());
        if (compilationUpdateRequest.getTitle() != null)
            old.setTitle(compilationUpdateRequest.getTitle());

        var updated = compilationRepository.save(old);
        return compilationMapper.mapToCompilationDto(updated);
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        var compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new CompilationNotExistException("Подборка не существует"));
        return compilationMapper.mapToCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned,
                                                Integer from,
                                                Integer size) {
        Predicate isPinned;
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Compilation.class);
        var compilationRoot = query.from(Compilation.class);
        var criteria = criteriaBuilder.conjunction();

        if (pinned != null) {
            if (pinned)
                isPinned = criteriaBuilder.isTrue(compilationRoot.get("pinned"));
            else
                isPinned = criteriaBuilder.isFalse(compilationRoot.get("pinned"));

            criteria = criteriaBuilder.and(criteria, isPinned);
        }
        query.select(compilationRoot).where(criteria);
        var compilations = entityManager.createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();

        return compilationMapper.mapToCompilationDtos(compilations);
    }
}
