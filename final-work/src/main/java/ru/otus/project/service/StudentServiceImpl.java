package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.converter.AccountConverter;
import ru.otus.project.converter.StudentConverter;
import ru.otus.project.domain.Student;
import ru.otus.project.dto.AccountDto;
import ru.otus.project.dto.StudentDto;
import ru.otus.project.exception.NoZeroIdException;
import ru.otus.project.exception.StudentNotFoundException;
import ru.otus.project.exception.ZeroIdException;
import ru.otus.project.repository.StudentRepository;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{

    private final AccountServiceImpl accountService;

    private final StudentRepository studentRepository;

    private final StudentConverter studentConverter;

    private final AccountConverter accountConverter;

    @Override
    @Transactional(readOnly = true)
    public StudentDto findByAccountId(long accountId) {
        return studentRepository.findByAccountId(accountId).map(studentConverter::entityToDto)
                .orElseThrow(() -> new StudentNotFoundException("Студент не найден"));
    }

    @Override
    @Transactional
    public StudentDto insert(StudentDto studentDto) {
        if (studentDto.getId() != 0) {
            throw new NoZeroIdException("Id должен равняться нулю");
        }

        AccountDto account = accountService.insert(studentDto.getAccount());
        Student student = studentRepository.save(new Student(
                studentDto.getId(),
                studentDto.getFirstName(),
                studentDto.getLastName(),
                studentDto.getStudyPlace(),
                accountConverter.dtoToEntity(account)
        ));
        return studentConverter.entityToDto(student);
    }

    @Override
    @Transactional
    public StudentDto update(StudentDto studentDto) {
        AccountDto accountDto = studentDto.getAccount();
        Student student = studentRepository.findByAccountId(accountDto.getId())
                .orElseThrow(() -> new StudentNotFoundException("Студент не найден"));

        AccountDto account = accountService.update(studentDto.getAccount());
        Student newStudent = studentRepository.save(new Student(
                student.getId(),
                studentDto.getFirstName(),
                studentDto.getLastName(),
                studentDto.getStudyPlace(),
                accountConverter.dtoToEntity(account))
        );
        return studentConverter.entityToDto(newStudent);
    }
}
