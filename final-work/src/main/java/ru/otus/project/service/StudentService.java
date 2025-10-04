package ru.otus.project.service;

import ru.otus.project.dto.StudentDto;

public interface StudentService {

    StudentDto findByAccountId(long accountId);

    StudentDto insert(StudentDto studentDto);

    StudentDto update(StudentDto studentDto);
}
