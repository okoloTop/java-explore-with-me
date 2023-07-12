package ru.practicum.evm.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.evm.event.dto.LongEventDto;
import ru.practicum.evm.event.dto.NewEventDto;
import ru.practicum.evm.event.dto.ShortEventDto;
import ru.practicum.evm.event.model.Event;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(source = "category", target = "category.id")
    Event toEvent(NewEventDto newEventDto);

    LongEventDto toLongEventDto(Event event);

    List<ShortEventDto> toShortEventDtos(List<Event> events);

    List<LongEventDto> toLongEventDtos(List<Event> events);
}
