package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@SuppressWarnings("unused")
@RequiredArgsConstructor
@Controller
public class GenreContoller {

    private final GenreService genreService;

    @GetMapping("/genres")
    public String findAllGenres(Model model) {
        List<GenreDto> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "genres";
    }
}
