package ru.otus.hw.exceptions;

public class BookNothingUpdateException extends RuntimeException {
    public BookNothingUpdateException(String message) {
        super(message);
    }
}
