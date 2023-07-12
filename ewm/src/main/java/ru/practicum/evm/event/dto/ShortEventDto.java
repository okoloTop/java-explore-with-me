package ru.practicum.evm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.evm.category.dto.CategoryDto;
import ru.practicum.evm.user.dto.ShortUserDto;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static ru.practicum.evm.utils.Patterns.DATE_PATTERN;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShortEventDto {
    private Long id;

    @JsonFormat(shape = STRING, pattern = DATE_PATTERN)
    private LocalDateTime eventDate;

    private ShortUserDto initiator;

    private Long confirmedRequests;

    private CategoryDto category;

    @Size(max = 2000)
    private String annotation;

    private Boolean paid;

    @Size(max = 120)
    private String title;

    private Long views;
}
