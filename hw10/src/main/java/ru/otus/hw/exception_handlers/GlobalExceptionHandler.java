package ru.otus.hw.exception_handlers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.exceptions.CommentNotFoundException;
import ru.otus.hw.exceptions.GenreNotFoundException;
import ru.otus.hw.exceptions.GenresIsEmptyException;
import ru.otus.hw.exceptions.NotEmptyIdInInsert;

@SuppressWarnings("unused")
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthorNotFoundException.class)
    public ModelAndView authorNotFound() {
        return new ModelAndView("error_page", "errorText",
                "Author not found");
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ModelAndView bookNotFound() {
        return new ModelAndView("error_page", "errorText",
                "Book not found");
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ModelAndView commentNotFound() {
        return new ModelAndView("error_page", "errorText",
                "Comment not found");
    }

    @ExceptionHandler(GenreNotFoundException.class)
    public ModelAndView genresNotFound() {
        return new ModelAndView("error_page", "errorText",
                "One of genres not found");
    }

    @ExceptionHandler(GenresIsEmptyException.class)
    public ModelAndView genresIsEmpty() {
        return new ModelAndView("error_page", "errorText",
                "Genres should be are not empty");
    }

    @ExceptionHandler(NotEmptyIdInInsert.class)
    public ModelAndView notEmptyInInsert() {
        return new ModelAndView("error_page", "errorText",
                "Id should be empty when adding");
    }
}
