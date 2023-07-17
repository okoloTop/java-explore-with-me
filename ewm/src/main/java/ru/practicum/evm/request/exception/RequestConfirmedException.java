package ru.practicum.evm.request.exception;


public class RequestConfirmedException extends RuntimeException {
    public RequestConfirmedException(String message) {
        super(message);
    }
}
