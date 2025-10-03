package ru.otus.project.service;

import ru.otus.project.domain.Course;
import ru.otus.project.domain.Lector;
import ru.otus.project.domain.StudyGroup;
import ru.otus.project.dto.LectureDto;

import java.util.List;

public interface LectureService {

    LectureDto findById(long id);

    List<LectureDto> findAllByGroupId(long lectorId);

    List<LectureDto> findAllByLectorAccountId(long lectorAccountId);

    LectureDto updateLectureByLector(LectureDto lectureDto, long accountId);

    void createDefaultLecturesForGroup(StudyGroup studyGroup);

    void assignLectorOnLecture(long lectureId, long lectorAccountId);

    void removeLectorFromLecture(long lectureId, long lectorAccountId);

    void removeLectorForCourseLectures(Course course, Lector oldLector);
}
