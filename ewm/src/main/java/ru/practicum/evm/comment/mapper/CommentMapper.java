package ru.practicum.evm.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.evm.comment.dto.CommentDto;
import ru.practicum.evm.comment.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "event.id", target = "eventId")
    CommentDto toCommentDto(Comment comment);

    List<CommentDto> toCommentDtos(List<Comment> comments);
}
