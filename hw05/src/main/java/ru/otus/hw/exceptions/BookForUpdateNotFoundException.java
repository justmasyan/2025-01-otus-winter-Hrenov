package ru.otus.hw.exceptions;

public class BookForUpdateNotFoundException extends RuntimeException {
    public BookForUpdateNotFoundException(String message) {
        super(message);
    }
}
