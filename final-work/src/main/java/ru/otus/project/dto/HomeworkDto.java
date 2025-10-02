package ru.otus.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkDto {

    private long id;

    private long mark;

    private HomeworkStatusDto status;

    private LectureDto lecture;

    private StudentDto student;

    private LectorDto lector;

    public HomeworkDto(long id, long mark, HomeworkStatusDto status) {
        this.id = id;
        this.mark = mark;
        this.status = status;
    }

    public HomeworkDto(long id, long mark, HomeworkStatusDto status, StudentDto student) {
        this.id = id;
        this.mark = mark;
        this.status = status;
        this.student = student;
    }

    public HomeworkDto(long id, long mark, HomeworkStatusDto status, StudentDto student, LectorDto lector) {
        this.id = id;
        this.mark = mark;
        this.status = status;
        this.student = student;
        this.lector = lector;
    }

    public HomeworkDto(long id, long mark, HomeworkStatusDto status, LectureDto lecture, StudentDto student) {
        this.id = id;
        this.mark = mark;
        this.status = status;
        this.lecture = lecture;
        this.student = student;
    }
}
