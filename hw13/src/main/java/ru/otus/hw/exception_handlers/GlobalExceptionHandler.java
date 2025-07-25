package ru.otus.hw.exception_handlers;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.exceptions.GenreNotFoundException;
import ru.otus.hw.exceptions.GenresIsEmptyException;

@SuppressWarnings("unused")
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView accessDenied() {
        return new ModelAndView("error_page","errorText",
                "Access Denied");
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ModelAndView authorNotFound() {
        return new ModelAndView("error_page","errorText",
                "Author not found");
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ModelAndView bookNotFound() {
        return new ModelAndView("error_page","errorText",
                "Book not found");
    }

    @ExceptionHandler(GenreNotFoundException.class)
    public ModelAndView genresNotFound() {
        return new ModelAndView("error_page","errorText",
                "One of genres not found");
    }

    @ExceptionHandler(GenresIsEmptyException.class)
    public ModelAndView genresIsEmpty() {
        return new ModelAndView("error_page","errorText",
                "Genres should be are not empty");
    }
}
