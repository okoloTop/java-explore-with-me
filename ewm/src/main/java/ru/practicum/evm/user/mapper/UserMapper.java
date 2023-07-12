package ru.practicum.evm.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.evm.user.dto.UserDto;
import ru.practicum.evm.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserDto userModelDto);

    UserDto toUserDto(User user);

    List<UserDto> toUserDtos(List<User> usersList);
}

