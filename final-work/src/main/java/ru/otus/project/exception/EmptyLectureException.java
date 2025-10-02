package ru.otus.project.exception;

public class EmptyLectureException extends RuntimeException{
    public EmptyLectureException(String message) {
        super(message);
    }
}
