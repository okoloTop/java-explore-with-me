package ru.practicum.evm.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.evm.request.dto.RequestDto;
import ru.practicum.evm.request.model.Request;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestDto toRequestDto(Request request);

    List<RequestDto> toRequestDtos(List<Request> requests);
}
