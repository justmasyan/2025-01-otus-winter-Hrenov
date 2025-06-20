package ru.otus.hw.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.GenreContoller;
import ru.otus.hw.services.GenreService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreContoller.class)
@Import(SecurityConfiguration.class)
@MockitoBean(types = {GenreService.class})
class GenreControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    private final static String URL = "http://localhost/%s";

    @Test
    void findAllGenres() throws Exception {
        mvc.perform(get("/genres").with(anonymous()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(URL.formatted("login")));

        mvc.perform(get("/genres").with(user("user")))
                .andExpect(status().isOk());
    }
}