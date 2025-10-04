package ru.otus.project.exception;

public class LectorNotFoundException extends RuntimeException {
    public LectorNotFoundException(String message) {
        super(message);
    }
}
