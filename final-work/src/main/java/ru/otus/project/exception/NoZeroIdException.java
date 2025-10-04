package ru.otus.project.exception;

public class NoZeroIdException extends RuntimeException{
    public NoZeroIdException(String message) {
        super(message);
    }
}
