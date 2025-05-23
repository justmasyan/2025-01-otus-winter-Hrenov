package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.services.CommentaryService;

import java.util.List;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@RestController
public class CommentaryController {

    private final CommentaryService commentaryService;

    @GetMapping("/comments/{id}")
    public CommentaryDto findCommentary(@PathVariable Long id) {
        return commentaryService.findById(id).orElseThrow(RuntimeException::new);
    }

    @GetMapping("/comments")
    public List<CommentaryDto> findAllCommentariesByBookId(@RequestParam Long bookId) {
        List<CommentaryDto> comments = commentaryService.findAllByBookId(bookId);
        return commentaryService.findAllByBookId(bookId);
    }

    @PostMapping("/comments")
    public CommentaryDto insertComment(@RequestBody CommentaryDto commentaryDto) {
        return commentaryService.save(commentaryDto);
    }

    @PutMapping("/comments/{id}")
    public CommentaryDto updateComment(@RequestBody CommentaryDto commentaryDto) {
        return commentaryService.save(commentaryDto);
    }

    @DeleteMapping("/comments/{id}")
    public void deleteCommentById(@PathVariable long id) {
        commentaryService.deleteById(id);
    }
}
