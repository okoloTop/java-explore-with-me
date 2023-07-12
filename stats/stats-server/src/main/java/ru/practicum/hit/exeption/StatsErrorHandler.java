package ru.practicum.hit.exeption;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.hit.utils.Patterns;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class StatsErrorHandler {

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public Error handleEventNotPublishedException(final WrongRequestDataException exception) {
        return Error.builder()
                .status(BAD_REQUEST.getReasonPhrase().toUpperCase())
                .reason("Conditions are wrong")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Patterns.DATE_PATTERN)))
                .build();
    }
}
