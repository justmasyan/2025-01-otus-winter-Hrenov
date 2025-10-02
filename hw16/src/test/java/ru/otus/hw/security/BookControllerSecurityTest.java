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
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.services.BookService;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
@MockitoBean(types = {BookService.class})
class BookControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    private final static String URL = "http://localhost/%s";

    @ParameterizedTest
    @MethodSource("getTestData")
    void shouldReturnExpectedParams(String url, String method, Boolean withParams, Boolean isAuthorised, Integer expectedStatus, String expectedRedirectedUrl) throws Exception {
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
            request.param("title", "MY_TITLE")
                    .param("authorId", "1")
                    .param("genresIds", "1,2,3");
        }
        return request;
    }

    static private Stream<Arguments> getTestData() {
        Integer bookId = 1;
        return Stream.of(
                //String url, String method, Boolean withParams, Boolean isAuthorised, Integer expectedStatus, String redirectedUrl
                Arguments.of("/books/%d".formatted(bookId), "get", false, true, 200, null),
                Arguments.of("/books/%d".formatted(bookId), "get", false, false, 302, URL.formatted("login")),

                Arguments.of("/books", "get", false, true, 200, null),
                Arguments.of("/books", "get", false, false, 302, URL.formatted("login")),

                Arguments.of("/books", "post", true, true, 302, "/books"),
                Arguments.of("/books", "post", true, false, 302, URL.formatted("login")),

                Arguments.of("/books/%d".formatted(bookId), "put", true, true, 302, "/books"),
                Arguments.of("/books/%d".formatted(bookId), "put", true, false, 302, URL.formatted("login")),

                Arguments.of("/books/%d".formatted(bookId), "delete", false, true, 302, "/"),
                Arguments.of("/books/%d".formatted(bookId), "delete", false, false, 302, URL.formatted("login"))
        );
    }
}