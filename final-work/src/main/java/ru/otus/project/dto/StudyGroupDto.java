package ru.otus.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudyGroupDto {

    private long id;

    private String code;

    private Date startDate;

    private CourseDto course;

    private List<StudentDto> students;

    public StudyGroupDto(long id, String code, Date startDate) {
        this.id = id;
        this.code = code;
        this.startDate = startDate;
    }

    public StudyGroupDto(long id, String code, Date startDate, CourseDto course) {
        this.id = id;
        this.code = code;
        this.startDate = startDate;
        this.course = course;
    }
}
