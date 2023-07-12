package ru.practicum.evm.compilation.dto;

import lombok.*;
import ru.practicum.evm.event.dto.ShortEventDto;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private Long id;
    private String title;
    private Boolean pinned;
    private List<ShortEventDto> events;
}
