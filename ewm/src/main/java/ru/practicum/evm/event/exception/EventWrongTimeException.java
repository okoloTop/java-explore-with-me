package ru.practicum.evm.event.exception;

public class EventWrongTimeException extends RuntimeException {
    public EventWrongTimeException(String message) {
        super(message);
    }
}
