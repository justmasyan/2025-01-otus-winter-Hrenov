package ru.otus.hw.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.MainPageController;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainPageController.class)
@Import(SecurityConfiguration.class)
class MainPageControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    private final static String URL = "http://localhost/%s";

    @Test
    void showActions() throws Exception {
        mvc.perform(get("/").with(anonymous()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(URL.formatted("login")));

        mvc.perform(get("/").with(user("user")))
                .andExpect(status().isOk());
    }
}