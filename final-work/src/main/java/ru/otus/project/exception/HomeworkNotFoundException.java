package ru.otus.project.exception;

public class HomeworkNotFoundException extends RuntimeException{
    public HomeworkNotFoundException(String message) {
        super(message);
    }
}
