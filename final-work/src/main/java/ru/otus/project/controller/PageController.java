package ru.otus.project.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Objects;

@Controller
public class PageController {

    @GetMapping("/")
    public String mainPage() {
        return "main_page";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
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
        return "account_courses";
    }

    @GetMapping("/courses/{id}")
    public String coursePage(@AuthenticationPrincipal Jwt jwt,
                             @PathVariable long id, Model model) {
        if (Objects.isNull(jwt)) {
            return "course";
        }
        model.addAttribute("accountId", jwt.getClaim("accountId"));
        return getPageByAuthentication(jwt.getClaim("authority"), "student_course",
                "lector_course", "authentication");
    }

    @GetMapping("/groups/{id}")
    public String groupPage(@PathVariable long id) {
        return "study_group";
    }

    @GetMapping("/lectures/{id}")
    public String lecturePage(@AuthenticationPrincipal Jwt jwt,
                              @PathVariable long id) {
        return getPageByAuthentication(jwt.getClaim("authority"), "student_lecture",
                "lector_lecture", "authentication");
    }

    @GetMapping("/homeworks/{id}")
    public String homeworkPage(@AuthenticationPrincipal Jwt jwt,
                               @PathVariable long id) {
        return getPageByAuthentication(jwt.getClaim("authority"), "student_homework",
                "lector_homework", "authentication");
    }

    @GetMapping("/account")
    public String accountPage(@AuthenticationPrincipal Jwt jwt, Model model) {
        model.addAttribute("accountId", jwt.getClaim("accountId"));
        return getPageByAuthentication(jwt.getClaim("authority"), "student",
                "lector", "authentication");
    }

    private String getPageByAuthentication(String authority, String pageForStudent,
                                           String pageForLector, String errorPage) {
        return switch (authority) {
            case "ROLE_STUDENT" -> pageForStudent;
            case "ROLE_LECTOR" -> pageForLector;
            default -> errorPage;
        };
    }
}
