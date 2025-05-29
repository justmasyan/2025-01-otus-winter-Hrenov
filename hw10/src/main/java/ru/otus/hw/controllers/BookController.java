package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.services.BookService;

import java.util.List;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    public List<BookDto> findAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/books/{id}")
    public BookDto findBookById(@PathVariable long id) {
        return bookService.findById(id).orElseThrow(
                () -> new BookNotFoundException("Book with id %d not found".formatted(id))
        );
    }

    @PostMapping("/books")
    public BookDto insertBook(@RequestBody BookDto bookDto) {
        return bookService.insert(bookDto);
    }

    @PutMapping("/books/{id}")
    public BookDto updateBook(@RequestBody BookDto bookDto) {
        return bookService.update(bookDto);
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
    }
}
