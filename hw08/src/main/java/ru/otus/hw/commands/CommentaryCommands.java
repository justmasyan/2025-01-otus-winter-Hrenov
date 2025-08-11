package ru.otus.hw.commands;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentaryConverter;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.services.CommentaryService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@AllArgsConstructor
@ShellComponent
public class CommentaryCommands {

    private final CommentaryService commentaryService;

    private final CommentaryConverter commentaryConverter;

    @ShellMethod(value = "Find commentary by id", key = "cbid")
    public String findCommentary(String id) {
        return commentaryService.findById(id)
                .map(commentaryConverter::commentaryDtoToString)
                .orElse("Commentary with id %s not found".formatted(id));
    }

    @ShellMethod(value = "Find all Comments by Book Id", key = "acbid")
    public String findAllCommentariesByBookId(String bookId) {
        return commentaryService.findAllByBookId(bookId).stream()
                .map(commentaryConverter::commentaryDtoToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Insert commentary", key = "cins")
    public String insertComment(String bookId, String text) {
        try {
            var commentary = commentaryService.insert(bookId, text);
            return commentaryConverter.commentaryDtoToString(commentary);
        } catch (BookNotFoundException e) {
            return "Not found book for Commentary";
        }
    }

    @ShellMethod(value = "Update commentary", key = "cupd")
    public String updateComment(String id, String bookId, String text) {
        try {
            var commentary = commentaryService.update(id, bookId, text);
            return commentaryConverter.commentaryDtoToString(commentary);
        } catch (BookNotFoundException e) {
            return "Not found book for Commentary";
        }
    }

    @ShellMethod(value = "Delete commentary by Id", key = "cdel")
    public void deleteCommentById(String id) {
        commentaryService.deleteById(id);
    }
}
