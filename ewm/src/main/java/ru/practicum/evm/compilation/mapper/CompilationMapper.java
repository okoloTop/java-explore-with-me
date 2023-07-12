package ru.practicum.evm.compilation.mapper;

import org.mapstruct.Mapper;
import ru.practicum.evm.compilation.dto.CompilationDto;
import ru.practicum.evm.compilation.model.Compilation;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompilationMapper {
    CompilationDto mapToCompilationDto(Compilation compilation);

    List<CompilationDto> mapToCompilationDtos(List<Compilation> compilations);
}
