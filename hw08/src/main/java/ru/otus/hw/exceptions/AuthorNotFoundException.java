package ru.otus.hw.exceptions;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String message) {
        super(message);
    }
}
