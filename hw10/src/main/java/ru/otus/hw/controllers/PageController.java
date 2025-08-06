package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentaryService;
import ru.otus.hw.services.GenreService;

@SuppressWarnings("unused")
@Controller
@RequiredArgsConstructor
public class PageController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final CommentaryService commentaryService;

    @GetMapping("/")
    public String showPages() {
        return "main_page";
    }

    @GetMapping("/authors")
    public String showAuthorPage() {
        return "authors";
    }

    @GetMapping("/genres")
    public String showGenrePage() {
        return "genres";
    }

    @GetMapping("/books/{id}")
    public String showBookPage(@PathVariable long id, Model model) {
        model.addAttribute("id", id);
        return "edit_book";
    }

    @GetMapping("/books/new")
    public String showAddBookPage() {
        return "add_book";
    }

    @GetMapping("/books")
    public String showBooksPage() {
        return "books";
    }

    @GetMapping("/comments/{id}")
    public String showCommentPage(@PathVariable long id, Model model) {
        model.addAttribute("id", id);
        return "edit_comment";
    }

    @GetMapping("/comments/book/{bookId}/new")
    public String showAddCommentPage(@PathVariable long bookId, Model model) {
        model.addAttribute("bookId", bookId);
        return "add_comment";
    }

    @GetMapping("/comments/book/{bookId}")
    public String showCommentsPage(@PathVariable long bookId, Model model) {
        model.addAttribute("bookId", bookId);
        return "comments";
    }

}
