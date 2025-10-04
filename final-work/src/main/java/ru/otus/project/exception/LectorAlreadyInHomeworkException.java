package ru.otus.project.exception;

public class LectorAlreadyInHomeworkException extends RuntimeException {
    public LectorAlreadyInHomeworkException(String message) {
        super(message);
    }
}
