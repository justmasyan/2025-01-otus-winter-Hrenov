package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@Controller
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    public String findAllBooks(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/books/{id}")
    public String findBookById(@PathVariable long id, Model model) {
        Optional<BookDto> book = bookService.findById(id);

        if (book.isEmpty()) {
            model.addAttribute("errorText", "Book with id %d not found".formatted(id));
            return "error_page";
        }

        model.addAttribute("book", book.get());
        return "book";
    }

    @PostMapping("/books")
    @Secured({"ROLE_ADMIN"})
    public String insertBook(@RequestParam String title,
                             @RequestParam long authorId,
                             @RequestParam Set<Long> genresIds) {
        bookService.insert(title, authorId, genresIds);
        return "redirect:/books";
    }

    @PutMapping("/books/{id}")
    public String updateBook(@PathVariable long id,
                             @RequestParam String title,
                             @RequestParam long authorId,
                             @RequestParam Set<Long> genresIds) {
        bookService.update(id, title, authorId, genresIds);
        return "redirect:/books";
    }

    @DeleteMapping("/books/{id}")
    public String deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }
}
