package ru.otus.project.exception;

public class NotZeroIdWhenInsertException extends RuntimeException{
    public NotZeroIdWhenInsertException(String message) {
        super(message);
    }
}
