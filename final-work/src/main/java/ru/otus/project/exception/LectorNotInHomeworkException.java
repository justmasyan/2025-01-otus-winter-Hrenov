package ru.otus.project.exception;

public class LectorNotInHomeworkException extends RuntimeException {
    public LectorNotInHomeworkException(String message) {
        super(message);
    }
}
