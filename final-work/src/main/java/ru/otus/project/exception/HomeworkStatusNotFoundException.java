package ru.otus.project.exception;

public class HomeworkStatusNotFoundException extends RuntimeException {
    public HomeworkStatusNotFoundException(String message) {
        super(message);
    }
}
