package ru.otus.hw.exceptions;

public class NotEmptyIdInInsert extends RuntimeException {
    public NotEmptyIdInInsert(String message) {
        super(message);
    }
}
