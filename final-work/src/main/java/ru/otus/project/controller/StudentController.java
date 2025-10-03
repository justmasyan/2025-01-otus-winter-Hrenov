package ru.otus.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.dto.StudentDto;
import ru.otus.project.service.StudentServiceImpl;

@RestController
@RequiredArgsConstructor
public class StudentController {

    private final StudentServiceImpl studentService;

    @GetMapping("api/students/accounts/{accountId}")
    public StudentDto findById(@PathVariable long accountId){
        return studentService.findByAccountId(accountId);
    }

    @PostMapping("api/students/new")
    public StudentDto insertStudent(@Valid @RequestBody StudentDto studentDto){
        return studentService.insert(studentDto);
    }

    @PutMapping("api/students/accounts/{accountId}")
    public StudentDto updateStudent(@RequestBody StudentDto studentDto){
        return studentService.update(studentDto);
    }
}
