package ru.practicum.evm.request.exception;

public class RequestParticipantLimitException extends RuntimeException {
    public RequestParticipantLimitException(String message) {
        super(message);
    }
}
