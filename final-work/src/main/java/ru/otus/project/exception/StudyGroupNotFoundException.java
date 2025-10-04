package ru.otus.project.exception;

public class StudyGroupNotFoundException extends RuntimeException {
    public StudyGroupNotFoundException(String message) {
        super(message);
    }
}
