package ru.otus.project.exception;

public class TopicTitleExistException extends RuntimeException {
    public TopicTitleExistException(String message) {
        super(message);
    }
}
