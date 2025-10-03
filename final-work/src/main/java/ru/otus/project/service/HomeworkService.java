package ru.otus.project.service;

import ru.otus.project.domain.Course;
import ru.otus.project.domain.Lector;
import ru.otus.project.domain.Student;
import ru.otus.project.domain.StudyGroup;
import ru.otus.project.dto.HomeworkDto;

import java.util.List;

public interface HomeworkService {

    HomeworkDto findById(long id);

    List<HomeworkDto> findAllByLectureId(long lectureId);

    List<HomeworkDto> findAllByLector(long lectorAccountId);

    List<HomeworkDto> findAllByStudent(long studentAccount);

    HomeworkDto findByLectureIdAndStudentId(long lectureId, long studentId);

    List<HomeworkDto> findByLectureIdAndLectorId(long lectureId, long lectorId);

    HomeworkDto updateHomework(HomeworkDto homeworkDto, long accountId);

    void createHomeworksForNewStudent(Student student, StudyGroup studyGroup);

    void deleteHomeworksForStudentAndGroup(Student student, StudyGroup studyGroup);

    void removeLectorForCourseHomeworks(Course course, Lector oldLector);

    void assignLectorToHomework(long homeworkId, long lectorAccountId);

    void removeLectorFromHomework(long homeworkId, long lectorAccountId);
}
