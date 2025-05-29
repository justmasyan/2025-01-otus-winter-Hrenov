package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.exceptions.CommentNotFoundException;
import ru.otus.hw.services.CommentaryService;

import java.util.List;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@RestController
public class CommentaryController {

    private final CommentaryService commentaryService;

    @GetMapping("/comments/{id}")
    public CommentaryDto findCommentary(@PathVariable Long id) {
        return commentaryService.findById(id).orElseThrow(
                () -> new CommentNotFoundException("Commentary with id %d not found".formatted(id))
        );
    }

    @GetMapping("/comments/book/{bookId}")
    public List<CommentaryDto> findAllCommentariesByBookId(@PathVariable Long bookId) {
        List<CommentaryDto> comments = commentaryService.findAllByBookId(bookId);
        return commentaryService.findAllByBookId(bookId);
    }

    @PostMapping("/comments")
    public CommentaryDto insertComment(@RequestBody CommentaryDto commentaryDto) {
        return commentaryService.insert(commentaryDto);
    }

    @PutMapping("/comments/{id}")
    public CommentaryDto updateComment(@RequestBody CommentaryDto commentaryDto) {
        return commentaryService.update(commentaryDto);
    }

    @DeleteMapping("/comments/{id}")
    public void deleteCommentById(@PathVariable long id) {
        commentaryService.deleteById(id);
    }
}
