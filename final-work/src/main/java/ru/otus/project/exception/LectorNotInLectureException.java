package ru.otus.project.exception;

public class LectorNotInLectureException extends RuntimeException {
    public LectorNotInLectureException(String message) {
        super(message);
    }
}
