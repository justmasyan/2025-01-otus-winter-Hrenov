package ru.otus.hw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SuppressWarnings("unused")
@Controller
public class PageController {

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
    public String showBookPage() {
        return "books";
    }

    @GetMapping("/api/comments")
    public String showCommentPage() {
        return "comments";
    }
}
