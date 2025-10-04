package ru.otus.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.dto.LectorDto;
import ru.otus.project.service.LectorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LectorController {

    private final LectorService lectorService;

    @GetMapping("api/lectors/accounts/{accountId}")
    public LectorDto findByAccountId(@PathVariable long accountId) {
        return lectorService.findByAccountId(accountId);
    }

    @GetMapping("api/lectors/courses/{courseId}")
    public List<LectorDto> findAllByCourseId(@PathVariable long courseId) {
        return lectorService.findAllByCourseId(courseId);
    }

    @PostMapping("api/lectors/new")
    public LectorDto insertLector(@RequestBody LectorDto lectorDto) {
        return lectorService.insert(lectorDto);
    }

    @PutMapping("api/lectors/accounts/{id}/new")
    public LectorDto updateLector(@RequestBody LectorDto lectorDto) {
        return lectorService.update(lectorDto);
    }
}
