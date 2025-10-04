package ru.otus.project.security;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.project.config.SecurityConfig;
import ru.otus.project.controller.CourseController;
import ru.otus.project.service.CourseService;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
@Import(SecurityConfig.class)
@MockitoBean(types = {CourseService.class})
class CourseControllerTest {

    @Autowired
    private MockMvc mvc;

    @ParameterizedTest
    @MethodSource("getTestData")
    void shouldReturnExpectedParams(String url, String method, GrantedAuthority authority, Integer expectedStatus) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = method2RequestBuilder(url, method);

        if (authority != null) {
            requestBuilder.with(jwt().authorities(authority).jwt(token -> token.claim("accountId", 1L)));
        }

        //Для создания
        requestBuilder.contentType(MediaType.APPLICATION_JSON)
                .content("{ \"id\": 1 }");

        mvc.perform(requestBuilder)
                .andExpect(status().is(expectedStatus));
    }

    private MockHttpServletRequestBuilder method2RequestBuilder(String url, String method) {
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap = Map.of(
                "get", MockMvcRequestBuilders::get,
                "post", MockMvcRequestBuilders::post,
                "put", MockMvcRequestBuilders::put,
                "delete", MockMvcRequestBuilders::delete
        );
        return methodMap.get(method).apply(url);
    }

    static private Stream<Arguments> getTestData() {
        long id = 1L;
        return Stream.of(
                Arguments.of("/api/courses/%d".formatted(id), "get", null, 200),
                Arguments.of("/api/courses/%d".formatted(id), "get", new SimpleGrantedAuthority("ROLE_STUDENT"), 200),
                Arguments.of("/api/courses/%d".formatted(id), "get", new SimpleGrantedAuthority("ROLE_LECTOR"), 200),

                Arguments.of("/api/courses", "get", null, 200),
                Arguments.of("/api/courses", "get", new SimpleGrantedAuthority("ROLE_STUDENT"), 200),
                Arguments.of("/api/courses", "get", new SimpleGrantedAuthority("ROLE_LECTOR"), 200),

                Arguments.of("/api/courses/lecture-topics/%d".formatted(id), "get", null, 200),
                Arguments.of("/api/courses/lecture-topics/%d".formatted(id), "get", new SimpleGrantedAuthority("ROLE_STUDENT"), 200),
                Arguments.of("/api/courses/lecture-topics/%d".formatted(id), "get", new SimpleGrantedAuthority("ROLE_LECTOR"), 200),

                Arguments.of("/api/courses/new", "post", null, 302),
                Arguments.of("/api/courses/new", "post", new SimpleGrantedAuthority("ROLE_STUDENT"), 403),
                Arguments.of("/api/courses/new", "post", new SimpleGrantedAuthority("ROLE_LECTOR"), 200),

                Arguments.of("/api/courses/%d/lector".formatted(id), "post", null, 302),
                Arguments.of("/api/courses/%d/lector".formatted(id), "post", new SimpleGrantedAuthority("ROLE_STUDENT"), 403),
                Arguments.of("/api/courses/%d/lector".formatted(id), "post", new SimpleGrantedAuthority("ROLE_LECTOR"), 200),

                Arguments.of("/api/courses/%d/lector".formatted(id), "delete", null, 302),
                Arguments.of("/api/courses/%d/lector".formatted(id), "delete", new SimpleGrantedAuthority("ROLE_STUDENT"), 403),
                Arguments.of("/api/courses/%d/lector".formatted(id), "delete", new SimpleGrantedAuthority("ROLE_LECTOR"), 200)

        );
    }
}