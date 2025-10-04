package ru.otus.project.exception;

public class LectureMaterialNotFoundException extends RuntimeException {
    public LectureMaterialNotFoundException(String message) {
        super(message);
    }
}
