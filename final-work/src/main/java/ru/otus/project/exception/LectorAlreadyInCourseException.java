package ru.otus.project.exception;

public class LectorAlreadyInCourseException extends RuntimeException {
    public LectorAlreadyInCourseException(String message) {
        super(message);
    }
}
