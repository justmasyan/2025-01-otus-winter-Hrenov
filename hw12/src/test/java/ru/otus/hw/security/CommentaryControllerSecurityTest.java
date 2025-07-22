package ru.otus.hw.security;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.controllers.CommentaryController;
import ru.otus.hw.services.CommentaryService;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentaryController.class)
@Import(SecurityConfiguration.class)
@MockitoBean(types = {CommentaryService.class})
class CommentaryControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    private final static String URL = "http://localhost/%s";

    private static final Integer BOOK_ID = 1;

    @ParameterizedTest
    @MethodSource("getTestData")
    void shouldReturnExpectedStatusAndExpectedRedirect(String url, String method, Boolean withParams, Boolean isAuthorised, Integer expectedStatus, String expectedRedirectedUrl) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = method2RequestBuilder(url, method, withParams);
        requestBuilder = isAuthorised ? requestBuilder.with(user("user"))
                : requestBuilder.with(anonymous());

        ResultActions result = mvc.perform(requestBuilder)
                .andExpect(status().is(expectedStatus));

        if (expectedStatus == HttpStatus.FOUND.value()) {
            result.andExpect(redirectedUrl(expectedRedirectedUrl));
        }
    }

    private MockHttpServletRequestBuilder method2RequestBuilder(String url, String method, Boolean withParams) {
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap = Map.of(
                "get", MockMvcRequestBuilders::get,
                "post", MockMvcRequestBuilders::post,
                "put", MockMvcRequestBuilders::put,
                "delete", MockMvcRequestBuilders::delete
        );
        MockHttpServletRequestBuilder request = methodMap.get(method).apply(url);

        if (withParams) {
            request.param("bookId", BOOK_ID.toString())
                    .param("text", "NEW_COMMENT");
        }
        return request;
    }

    static private Stream<Arguments> getTestData() {
        return Stream.of(
                //String url, String method, Boolean withParams, Boolean isAuthorised, Integer expectedStatus, String redirectedUrl
                Arguments.of("/comments/1", "get", false, true, 200, null),
                Arguments.of("/comments/1", "get", false, false, 302, URL.formatted("login")),

                Arguments.of("/comments?bookId=%d".formatted(BOOK_ID), "get", false, true, 200, null),
                Arguments.of("/comments?bookId=%d".formatted(BOOK_ID), "get", false, false, 302, URL.formatted("login")),

                Arguments.of("/comments", "post", true, true, 302, "/comments?bookId=%d".formatted(BOOK_ID)),
                Arguments.of("/comments", "post", true, false, 302, URL.formatted("login")),

                Arguments.of("/comments/1", "put", true, true, 302, "/comments?bookId=%d".formatted(BOOK_ID)),
                Arguments.of("/comments/1", "put", true, false, 302, URL.formatted("login")),

                Arguments.of("/comments/1", "delete", false, true, 302, "/"),
                Arguments.of("/comments/1", "delete", false, false, 302, URL.formatted("login"))
        );
    }
}