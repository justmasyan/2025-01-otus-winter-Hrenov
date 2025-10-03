package ru.otus.project.exception;

public class UndefinedRoleException extends RuntimeException {
    public UndefinedRoleException(String message) {
        super(message);
    }
}
