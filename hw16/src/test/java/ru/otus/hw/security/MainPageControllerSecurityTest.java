package ru.otus.hw.security;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.otus.hw.controllers.MainPageController;

import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainPageController.class)
@Import(SecurityConfiguration.class)
class MainPageControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @ParameterizedTest
    @MethodSource("getTestData")
    void showActions(Boolean isAuthorised, Integer expectedStatus) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get("/");
        requestBuilder = isAuthorised ? requestBuilder.with(user("user"))
                : requestBuilder.with(anonymous());

        ResultActions result = mvc.perform(requestBuilder)
                .andExpect(status().is(expectedStatus));

        if (expectedStatus == HttpStatus.FOUND.value()) {
            result.andExpect(redirectedUrlPattern("**/login"));
        }
    }

    static private Stream<Arguments> getTestData() {
        return Stream.of(
                Arguments.of(true, 200),
                Arguments.of(false, 302)
        );
    }
}