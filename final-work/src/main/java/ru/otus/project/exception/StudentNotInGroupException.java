package ru.otus.project.exception;

public class StudentNotInGroupException extends RuntimeException {
    public StudentNotInGroupException(String message) {
        super(message);
    }
}
