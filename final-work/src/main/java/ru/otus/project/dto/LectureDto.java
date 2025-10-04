package ru.otus.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureDto {

    private long id;

    private String url;

    private Date lectureTime;

    private LectureStatusDto status;

    private LectureTopicDto lectureTopic;

    private LectorDto lector;

    private StudyGroupDto studyGroup;

    public LectureDto(long id, String url, Date lectureTime, LectureStatusDto status, LectureTopicDto lectureTopic) {
        this.id = id;
        this.url = url;
        this.lectureTime = lectureTime;
        this.status = status;
        this.lectureTopic = lectureTopic;
    }

    public LectureDto(long id, String url, Date lectureTime, LectureTopicDto lectureTopic, StudyGroupDto studyGroup) {
        this.id = id;
        this.url = url;
        this.lectureTime = lectureTime;
        this.lectureTopic = lectureTopic;
        this.studyGroup = studyGroup;
    }

    public LectureDto(long id, String url, Date lectureTime, LectureStatusDto status, LectureTopicDto lectureTopic, StudyGroupDto studyGroup) {
        this.id = id;
        this.url = url;
        this.lectureTime = lectureTime;
        this.status = status;
        this.lectureTopic = lectureTopic;
        this.studyGroup = studyGroup;
    }
}
