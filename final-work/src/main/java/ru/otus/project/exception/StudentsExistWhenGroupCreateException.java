package ru.otus.project.exception;

public class StudentsExistWhenGroupCreateException extends RuntimeException {
    public StudentsExistWhenGroupCreateException(String message) {
        super(message);
    }
}
