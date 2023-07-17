package ru.practicum.evm.event.exception;


public class EventPublishedException extends RuntimeException {
    public EventPublishedException(String message) {
        super(message);
    }
}
