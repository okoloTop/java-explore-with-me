package ru.practicum.evm.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortUserDto {
    private Long id;
    private String name;
}
