package ru.otus.project.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.project.dto.AccountDto;

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
    public String coursesPage(@AuthenticationPrincipal AccountDto accountDto) {
        return "courses";
    }

    @GetMapping("/courses/{id}")
    public String coursePage(@AuthenticationPrincipal AccountDto accountDto,
                             @PathVariable long id, Model model) {
        if (Objects.isNull(accountDto)) {
            return "course";
        }
        model.addAttribute("accountId", accountDto.getId());
        return getPageByAuthentication(accountDto, "student_course",
                "lector_course", "authentication");
    }

    @GetMapping("/groups/{id}")
    public String groupPage(@PathVariable long id) {
        return "study_group";
    }

    @GetMapping("/lectures/{id}")
    public String lecturePage(@AuthenticationPrincipal AccountDto accountDto,
                              @PathVariable long id) {
        return getPageByAuthentication(accountDto, "student_lecture",
                "lector_lecture", "authentication");
    }

    @GetMapping("/homeworks/{id}")
    public String homeworkPage(@AuthenticationPrincipal AccountDto accountDto,
                               @PathVariable long id) {
        return getPageByAuthentication(accountDto, "student_homework",
                "lector_homework", "authentication");
    }

    @GetMapping("/account")
    public String accountPage(@AuthenticationPrincipal AccountDto accountDto, Model model) {
        model.addAttribute("accountId", accountDto.getId());
        return getPageByAuthentication(accountDto, "student",
                "lector", "authentication");
    }

    private String getPageByAuthentication(AccountDto accountDto, String pageForStudent,
                                           String pageForLector, String errorPage) {
        return switch (accountDto.getAuthority().getAuthority()) {
            case "ROLE_STUDENT" -> pageForStudent;
            case "ROLE_LECTOR" -> pageForLector;
            default -> errorPage;
        };
    }
}
