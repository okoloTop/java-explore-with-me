package ru.practicum.evm.request.exception;


public class RequestNotExistException extends RuntimeException {
    public RequestNotExistException(String message) {
        super(message);
    }
}
