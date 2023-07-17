package ru.practicum.hit.exeption;

public class WrongRequestDataException extends RuntimeException {
    public WrongRequestDataException(String message) {
        super(message);
    }
}
