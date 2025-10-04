package ru.otus.project.exception;

public class LectorAlreadyInLectureException extends RuntimeException {
    public LectorAlreadyInLectureException(String message) {
        super(message);
    }
}
