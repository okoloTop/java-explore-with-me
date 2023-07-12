package ru.practicum.evm.request.exception;

public class RequestExistException extends RuntimeException {
    public RequestExistException(String message) {
        super(message);
    }
}
