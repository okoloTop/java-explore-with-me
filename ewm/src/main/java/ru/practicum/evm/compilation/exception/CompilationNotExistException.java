package ru.practicum.evm.compilation.exception;

public class CompilationNotExistException extends RuntimeException {
    public CompilationNotExistException(String message) {
        super(message);
    }
}
