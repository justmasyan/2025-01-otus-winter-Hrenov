package ru.otus.project.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.project.domain.Student;
import ru.otus.project.dto.StudentDto;

@Component
@RequiredArgsConstructor
public class StudentConverter {

    private final AccountConverter accountConverter;

    public StudentDto entityToBaseInfoDto(Student student){
        return new StudentDto(student.getId(), student.getFirstName(),
                student.getLastName(), student.getStudyPlace());
    }

    public StudentDto entityToDto(Student student){
        return new StudentDto(student.getId(), student.getFirstName(),student.getLastName(),
                student.getStudyPlace(), accountConverter.entityToBaseInfoDto(student.getAccount()));
    }
}
