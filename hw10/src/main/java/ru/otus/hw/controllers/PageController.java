package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentaryService;
import ru.otus.hw.services.GenreService;

import java.util.List;

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

    @GetMapping("/api/authors")
    public String showAuthorPage() {
        return "authors";
    }

    @GetMapping("/api/genres")
    public String showGenrePage() {
        return "genres";
    }

    @GetMapping("/api/books")
    public String showBookPage(Model model) {
        List<AuthorDto> authors = authorService.findAll();
        List<GenreDto> genres = genreService.findAll();
        List<BookDto> books = bookService.findAllBaseInfo();

        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/api/comments")
    public String showCommentPage(Model model) {
        List<BookDto> books = bookService.findAllBaseInfo();
        List<CommentaryDto> comments = commentaryService.findAll();
        model.addAttribute("books", books);
        model.addAttribute("comments", comments);
        return "comments";
    }
}
