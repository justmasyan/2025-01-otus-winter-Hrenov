package ru.otus.hw.exceptions;

public class GenresIsEmptyException extends RuntimeException {
    public GenresIsEmptyException(String message) {
        super(message);
    }
}
