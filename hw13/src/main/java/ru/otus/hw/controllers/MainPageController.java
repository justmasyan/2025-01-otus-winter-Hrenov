package ru.otus.hw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SuppressWarnings("unused")
@Controller
public class MainPageController {

    @GetMapping("/")
    public String showActions() {
        return "main_page";
    }
}
