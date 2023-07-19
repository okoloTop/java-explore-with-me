package ru.practicum.evm.error;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.evm.category.exception.CategoryNotEmptyException;
import ru.practicum.evm.category.exception.CategoryNotExistException;
import ru.practicum.evm.comment.exception.CommentNotExistException;
import ru.practicum.evm.comment.exception.UsernameInCommentException;
import ru.practicum.evm.compilation.exception.CompilationNotExistException;
import ru.practicum.evm.event.exception.*;
import ru.practicum.evm.request.exception.RequestConfirmedException;
import ru.practicum.evm.request.exception.RequestExistException;
import ru.practicum.evm.request.exception.RequestNotExistException;
import ru.practicum.evm.request.exception.RequestParticipantLimitException;
import ru.practicum.evm.user.exception.NameExistException;
import ru.practicum.evm.user.exception.UserNotExistException;
import ru.practicum.evm.user.exception.WrongUserException;
import ru.practicum.evm.utils.Patterns;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public Error handleCategoryNotEmptyException(final CategoryNotEmptyException exception) {
        return Error.builder()
                .status(CONFLICT.getReasonPhrase().toUpperCase())
                .reason("Неверные условия")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public Error handleCategoryNotExistException(final CategoryNotExistException exception) {
        return Error.builder()
                .status(NOT_FOUND.getReasonPhrase().toUpperCase())
                .reason(("Категория не существует"))
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public Error handleRequestExistException(final RequestExistException exception) {
        return Error.builder()
                .status(CONFLICT.getReasonPhrase().toUpperCase())
                .reason("Неверный запрос")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public Error handleRequestConfirmedException(final RequestConfirmedException exception) {
        return Error.builder()
                .status(CONFLICT.getReasonPhrase().toUpperCase())
                .reason("Неверные условия")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public Error handleRequestParticipantLimitException(final RequestParticipantLimitException exception) {
        return Error.builder()
                .status(CONFLICT.getReasonPhrase().toUpperCase())
                .reason("Неверные условия")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public Error handleRequestNotExistException(final RequestNotExistException exception) {
        return Error.builder()
                .status(NOT_FOUND.getReasonPhrase().toUpperCase())
                .reason(("Запрос не существует"))
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public Error handleNameExistException(final NameExistException exception) {
        return Error.builder()
                .status(CONFLICT.getReasonPhrase().toUpperCase())
                .reason("Это имя пользователя занято")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public Error handleWrongUserException(final WrongUserException exception) {
        return Error.builder()
                .status(CONFLICT.getReasonPhrase().toUpperCase())
                .reason("Ошибка пользователя")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public Error handleUserNotExistException(final UserNotExistException exception) {
        return Error.builder()
                .status(NOT_FOUND.getReasonPhrase().toUpperCase())
                .reason("Пользователь не существует")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public Error handleEventNotPublishedException(final EventNotPublishedException exception) {
        return Error.builder()
                .status(CONFLICT.getReasonPhrase().toUpperCase())
                .reason("Неверные условия")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public Error handleEventPublishedException(final EventPublishedException exception) {
        return Error.builder()
                .status(CONFLICT.getReasonPhrase().toUpperCase())
                .reason("Неверные условия")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public Error handleEventCanceledException(final EventCanceledException exception) {
        return Error.builder()
                .status(CONFLICT.getReasonPhrase().toUpperCase())
                .reason("Неверные условия")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public Error handleEventWrongTimeException(final EventWrongTimeException exception) {
        return Error.builder()
                .status(BAD_REQUEST.getReasonPhrase().toUpperCase())
                .reason("Неверное время")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public Error handleEventNotExistException(final EventNotExistException exception) {
        return Error.builder()
                .status(NOT_FOUND.getReasonPhrase().toUpperCase())
                .reason(("Событие не существует"))
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public Error handleCompilationNotExistException(final CompilationNotExistException exception) {
        return Error.builder()
                .status(NOT_FOUND.getReasonPhrase().toUpperCase())
                .reason(("Подборка не существует"))
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public Error handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException exception) {
        return Error.builder()
                .status(BAD_REQUEST.getReasonPhrase().toUpperCase())
                .reason("Incorrectly made request")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public Error handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        return Error.builder()
                .status(BAD_REQUEST.getReasonPhrase().toUpperCase())
                .reason("Incorrectly made request")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public Error handleMissingServletRequestParameterException(final MissingServletRequestParameterException exception) {
        return Error.builder()
                .status(BAD_REQUEST.getReasonPhrase().toUpperCase())
                .reason("Incorrectly made request")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public Error handleEmptyResultDataAccessException(final EmptyResultDataAccessException exception) {
        return Error.builder()
                .status(NOT_FOUND.getReasonPhrase().toUpperCase())
                .reason("Empty result data")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public Error handleUnknownException(Exception exception) {
        return Error.builder()
                .status(INTERNAL_SERVER_ERROR.getReasonPhrase().toUpperCase())
                .reason("Unknown error")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }


    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public Error handleCommentNotExistException(final CommentNotExistException exception) {
        return Error.builder()
                .status(NOT_FOUND.getReasonPhrase().toUpperCase())
                .reason("This comment does not exist")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }


    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public Error handleUserNameExistException(final UsernameInCommentException exception) {
        return Error.builder()
                .status(CONFLICT.getReasonPhrase().toUpperCase())
                .reason("This username is already exist")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }
}
