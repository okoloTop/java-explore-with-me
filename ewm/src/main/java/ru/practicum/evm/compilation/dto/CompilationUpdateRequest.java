package ru.practicum.evm.compilation.dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationUpdateRequest {
    @Size(max = 50)
    private String title;
    private Boolean pinned;
    private List<Long> events;
}
