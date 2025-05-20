package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.services.CommentaryService;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@Controller
public class CommentaryController {

    private final CommentaryService commentaryService;

    @GetMapping("/comments/{id}")
    public String findCommentary(@PathVariable Long id, Model model) {
        Optional<CommentaryDto> comment = commentaryService.findById(id);

        if (comment.isEmpty()) {
            model.addAttribute("errorText", "Commentary with id %d not found".formatted(id));
            return "error_page";
        }

        model.addAttribute("comment", comment.get());
        return "comment";
    }

    @GetMapping("/comments")
    public String findAllCommentariesByBookId(@RequestParam Long bookId, Model model) {
        List<CommentaryDto> comments = commentaryService.findAllByBookId(bookId);
        model.addAttribute("comments", comments);
        model.addAttribute("bookId", bookId);
        return "comments";
    }

    @PostMapping("/comments")
    public String insertComment(@RequestParam Long bookId,
                                @RequestParam String text) {
        try {
            commentaryService.insert(bookId, text);
            return "redirect:/comments?bookId=%d".formatted(bookId);
        } catch (BookNotFoundException e) {
            return "Not found book for Commentary";
        }
    }

    @PutMapping("/comments/{id}")
    public String updateComment(@PathVariable Long id,
                                @RequestParam Long bookId,
                                @RequestParam String text) {
        try {
            commentaryService.update(id, bookId, text);
            return "redirect:/comments?bookId=%d".formatted(bookId);
        } catch (BookNotFoundException e) {
            return "Not found book for Commentary";
        }
    }

    @DeleteMapping("/comments/{id}")
    public String deleteCommentById(@PathVariable long id) {
        commentaryService.deleteById(id);
        return "redirect:/";
    }
}
