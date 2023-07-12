package ru.practicum.evm.compilation.service;

import ru.practicum.evm.compilation.dto.CompilationDto;
import ru.practicum.evm.compilation.dto.CompilationUpdateRequest;
import ru.practicum.evm.compilation.dto.SavedCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto saveCompilation(SavedCompilationDto savedCompilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId,
                                     CompilationUpdateRequest compilationUpdateRequest);

    CompilationDto getCompilation(Long compId);

    List<CompilationDto> getCompilations(Boolean pinned,
                                         Integer from,
                                         Integer size);
}
