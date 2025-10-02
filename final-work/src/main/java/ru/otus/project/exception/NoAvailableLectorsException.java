package ru.otus.project.exception;

public class NoAvailableLectorsException extends RuntimeException{

    public NoAvailableLectorsException(String message) {
        super(message);
    }
}
