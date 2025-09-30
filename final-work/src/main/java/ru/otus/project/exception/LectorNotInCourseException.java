package ru.otus.project.exception;

public class LectorNotInCourseException extends RuntimeException {
    public LectorNotInCourseException(String message) {
        super(message);
    }
}
