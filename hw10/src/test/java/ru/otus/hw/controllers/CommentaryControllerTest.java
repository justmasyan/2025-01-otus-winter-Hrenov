package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.services.CommentaryService;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentaryController.class)
class CommentaryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private CommentaryService commentaryService;


    private List<CommentaryDto> dbComments;

    @BeforeEach
    void setUp() {
        dbComments = getDbCommentaries();
    }

    @Test
    void findCommentary() throws Exception {
        CommentaryDto exceptedComment = dbComments.get(0);
        when(commentaryService.findById(1L)).thenReturn(Optional.ofNullable(exceptedComment));
        mvc.perform(get("/api/comments/%d".formatted(1L)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(exceptedComment)));
    }

    @Test
    void findAllCommentariesByBookId() throws Exception {
        List<CommentaryDto> exceptedComments = dbComments;
        when(commentaryService.findAllByBookId(1L)).thenReturn(exceptedComments);
        mvc.perform(get("/api/comments/book/%d".formatted(1L)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(exceptedComments)));
    }

    @Test
    void insertComment() throws Exception {
        CommentaryDto exceptedComment = dbComments.get(0);
        String exceptedCommentJson = mapper.writeValueAsString(exceptedComment);
        when(commentaryService.insert(any())).thenReturn(exceptedComment);

        mvc.perform(post("/api/comments").contentType(APPLICATION_JSON).content(exceptedCommentJson))
                .andExpect(status().isOk())
                .andExpect(content().json(exceptedCommentJson));
    }

    @Test
    void updateComment() throws Exception {
        CommentaryDto exceptedComment = dbComments.get(0);
        String exceptedCommentJson = mapper.writeValueAsString(exceptedComment);
        when(commentaryService.update(any())).thenReturn(exceptedComment);

        mvc.perform(put("/api/comments/%d".formatted(exceptedComment.getId()))
                        .contentType(APPLICATION_JSON).content(exceptedCommentJson))
                .andExpect(status().isOk())
                .andExpect(content().json(exceptedCommentJson));
    }

    @Test
    void deleteCommentById() throws Exception {
        CommentaryDto expectedComment = dbComments.get(0);
        mvc.perform(delete("/api/comments/%d".formatted(expectedComment.getId())))
                .andExpect(status().isOk());

        verify(commentaryService, times(1))
                .deleteById(expectedComment.getId());
    }

    private static List<CommentaryDto> getDbCommentaries() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new CommentaryDto(id, "Comment_" + id))
                .toList();
    }
}