package ru.otus.project.exception;

public class EmptyTopicsException extends RuntimeException{

    public EmptyTopicsException(String message) {
        super(message);
    }
}
