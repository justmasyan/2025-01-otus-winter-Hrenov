package ru.otus.hw.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.CommentaryController;
import ru.otus.hw.services.CommentaryService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentaryController.class)
@Import(SecurityConfiguration.class)
@MockitoBean(types = {CommentaryService.class})
class CommentaryControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    private final static String URL = "http://localhost/%s";

    @Test
    void findCommentary() throws Exception {
        mvc.perform(get("/comments/{id}", 1).with(anonymous()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(URL.formatted("login")));

        mvc.perform(get("/comments/{id}", 1).with(user("user")))
                .andExpect(status().isOk());
    }

    @Test
    void findAllCommentariesByBookId() throws Exception {
        mvc.perform(get("/comments?bookId={bookId}", 1L)
                        .with(anonymous()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(URL.formatted("login")));

        mvc.perform(get("/comments?bookId={bookId}", 1L)
                        .with(user("user")))
                .andExpect(status().isOk());
    }

    @Test
    void insertComment() throws Exception {
        mvc.perform(post("/comments")
                        .param("bookId", "1")
                        .param("text", "NEW_COMMENT")
                        .with(anonymous()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(URL.formatted("login")));

        mvc.perform(post("/comments")
                        .param("bookId", "1")
                        .param("text", "NEW_COMMENT")
                        .with(user("user")))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/comments?bookId=%s".formatted(1L)));
    }

    @Test
    void updateComment() throws Exception {
        mvc.perform(put("/comments/{id}",1L)
                        .param("bookId", "1")
                        .param("text", "NEW_COMMENT")
                        .with(anonymous()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(URL.formatted("login")));

        mvc.perform(put("/comments/{id}",1L)
                        .param("bookId", "1")
                        .param("text", "NEW_COMMENT")
                        .with(user("user")))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/comments?bookId=%s".formatted(1L)));
    }

    @Test
    void deleteCommentById() throws Exception {
        mvc.perform(delete("/comments/{id}", 1L).with(anonymous()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(URL.formatted("login")));

        mvc.perform(delete("/comments/{id}", 1L).with(user("user")))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));
    }
}