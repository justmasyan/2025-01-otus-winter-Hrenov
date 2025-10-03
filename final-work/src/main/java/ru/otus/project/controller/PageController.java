package ru.otus.project.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.project.exception.UndefinedRoleException;

import java.util.Objects;

@Controller
public class PageController {

    @GetMapping("/")
    public String mainPage() {
        return "main_page";
    }

    @GetMapping("/registration")
    public String registerPage() {
        return "registration";
    }

    @GetMapping("/authentication")
    public String authenticationPage() {
        return "authentication";
    }

    @GetMapping("/courses")
    public String coursesPage(@AuthenticationPrincipal Jwt jwt) {
        if (Objects.isNull(jwt)) {
            return "courses";
        }
        return getPageByAuthentication(jwt.getClaim("authority"),
                "student_courses", "lector_courses");
    }

    @GetMapping("/courses/{id}")
    public String coursePage(@AuthenticationPrincipal Jwt jwt,
                             @PathVariable long id, Model model) {
        if (Objects.isNull(jwt)) {
            return "course";
        }
        model.addAttribute("accountId", jwt.getClaim("accountId"));
        return getPageByAuthentication(jwt.getClaim("authority"),
                "student_course", "lector_course");
    }

    @GetMapping("/lecture-topics")
    public String topicsPage(@AuthenticationPrincipal Jwt jwt) {
        return "lecture_topics";
    }

    @GetMapping("/groups/{id}")
    public String groupPage(@AuthenticationPrincipal Jwt jwt,
                            @PathVariable long id) {
        return getPageByAuthentication(jwt.getClaim("authority"),
                "student_group", "study_group");
    }

    @GetMapping("/lectures/{id}")
    public String lecturePage(@AuthenticationPrincipal Jwt jwt,
                              @PathVariable long id) {
        return getPageByAuthentication(jwt.getClaim("authority"),
                "student_lecture", "lector_lecture");
    }

    @GetMapping("/homeworks/{id}")
    public String homeworkPage(@AuthenticationPrincipal Jwt jwt,
                               @PathVariable long id) {
        return getPageByAuthentication(jwt.getClaim("authority"),
                "student_homework", "lector_homework");
    }

    @GetMapping("/account")
    public String accountPage(@AuthenticationPrincipal Jwt jwt, Model model) {
        model.addAttribute("accountId", jwt.getClaim("accountId"));
        return getPageByAuthentication(jwt.getClaim("authority"),
                "student", "lector");
    }

    private String getPageByAuthentication(String authority, String pageForStudent,
                                           String pageForLector) {
        return switch (authority) {
            case "ROLE_STUDENT" -> pageForStudent;
            case "ROLE_LECTOR" -> pageForLector;
            default -> throw new UndefinedRoleException("Неизвестная роль");
        };
    }
}
