package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.converter.StudentConverter;
import ru.otus.project.domain.Account;
import ru.otus.project.domain.Student;
import ru.otus.project.dto.AccountDto;
import ru.otus.project.dto.StudentDto;
import ru.otus.project.repository.StudentRepository;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl {

    private final AccountServiceImpl accountService;

    private final StudentRepository studentRepository;

    private final StudentConverter studentConverter;

    @Transactional(readOnly = true)
    public StudentDto findByAccountId(long accountId) {
        return studentRepository.findByAccountId(accountId).map(studentConverter::entityToDto)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @Transactional
    public StudentDto insert(StudentDto studentDto) {
        if (studentDto.getId() != 0 || studentDto.getAccount().getId() != 0) {
            throw new RuntimeException("Id не должен равняться нулю");
        }
        return save(studentDto);
    }

    private StudentDto save(StudentDto studentDto) {
        AccountDto account = accountService.insert(studentDto.getAccount());
        Student student = studentRepository.save(new Student(
                studentDto.getId(),
                studentDto.getFirstName(),
                studentDto.getLastName(),
                studentDto.getStudyPlace(),
                new Account(account.getId())
        ));
        return studentConverter.entityToDto(student);
    }
}
