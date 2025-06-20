package ru.otus.hw.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.services.BookService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
@MockitoBean(types = {BookService.class})
class BookControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    private final static String URL = "http://localhost/%s";

    @Test
    void findAllBooks() throws Exception {
        mvc.perform(get("/books").with(anonymous()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(URL.formatted("login")));

        mvc.perform(get("/books").with(user("user")))
                .andExpect(status().isOk());
    }

    @Test
    void findBookById() throws Exception {
        mvc.perform(get("/books/{id}", 1).with(anonymous()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(URL.formatted("login")));

        mvc.perform(get("/books/{id}", 1).with(user("user")))
                .andExpect(status().isOk());
    }

    @Test
    void insertBook() throws Exception {
        mvc.perform(post("/books")
                        .param("title", "MY_TITLE")
                        .param("authorId", "1")
                        .param("genresIds", "1,2,3")
                        .with(anonymous()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(URL.formatted("login")));

        mvc.perform(post("/books")
                        .param("title", "MY_TITLE")
                        .param("authorId", "1")
                        .param("genresIds", "1,2,3")
                        .with(user("user")))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    void updateBook() throws Exception {
        mvc.perform(put("/books/{id}",1L)
                        .param("title", "MY_TITLE")
                        .param("authorId", "1")
                        .param("genresIds", "1,2,3")
                        .with(anonymous()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(URL.formatted("login")));

        mvc.perform(put("/books/{id}", 1L)
                        .param("title", "MY_TITLE")
                        .param("authorId", "1")
                        .param("genresIds", "1,2,3")
                        .with(user("user")))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    void deleteBook() throws Exception {
        mvc.perform(delete("/books/{id}", 1L).with(anonymous()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(URL.formatted("login")));

        mvc.perform(delete("/books/{id}", 1L).with(user("user")))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));
    }
}