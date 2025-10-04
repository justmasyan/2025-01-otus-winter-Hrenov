package ru.otus.project.exception;

public class AccountNotInHomeworkException extends RuntimeException {
    public AccountNotInHomeworkException(String message) {
        super(message);
    }
}
