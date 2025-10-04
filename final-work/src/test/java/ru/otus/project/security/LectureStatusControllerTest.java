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
import ru.otus.project.controller.LectureStatusController;
import ru.otus.project.service.LectureStatusService;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LectureStatusController.class)
@Import(SecurityConfig.class)
@MockitoBean(types = {LectureStatusService.class})
class LectureStatusControllerTest {

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
        return Stream.of(
                Arguments.of("/api/lecture-statuses", "get", null, 302),
                Arguments.of("/api/lecture-statuses", "get", new SimpleGrantedAuthority("ROLE_STUDENT"), 200),
                Arguments.of("/api/lecture-statuses", "get", new SimpleGrantedAuthority("ROLE_LECTOR"), 200)
        );
    }
}