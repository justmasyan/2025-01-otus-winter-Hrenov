package ru.otus.project.exception;

public class EmptyHomeworkException extends RuntimeException {
    public EmptyHomeworkException(String message) {
        super(message);
    }
}
