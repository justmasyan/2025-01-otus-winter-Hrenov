package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.converter.HomeworkConverter;
import ru.otus.project.converter.HomeworkStatusConverter;
import ru.otus.project.domain.Homework;
import ru.otus.project.domain.Lector;
import ru.otus.project.domain.Lecture;
import ru.otus.project.domain.Student;
import ru.otus.project.domain.StudyGroup;
import ru.otus.project.dto.HomeworkDto;
import ru.otus.project.enums.HomeworkStatusEnum;
import ru.otus.project.repository.HomeworkRepository;
import ru.otus.project.repository.LectorRepository;
import ru.otus.project.repository.LectureRepository;
import ru.otus.project.utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeworkServiceImpl {

    private final LectureRepository lectureRepository;

    private final LectorRepository lectorRepository;

    private final HomeworkRepository homeworkRepository;

    private final HomeworkConverter homeworkConverter;

    private final HomeworkStatusConverter statusConverter;

    @Transactional(readOnly = true)
    public HomeworkDto findById(long id) {
        return homeworkRepository.findById(id)
                .map(homeworkConverter::entityToDto)
                .orElseThrow(() -> new RuntimeException("Домашняя работа не найдена"));
    }

    @Transactional(readOnly = true)
    public List<HomeworkDto> findAllByLectorId(long lectorId) {
        return homeworkRepository.findAllByLectorAccountId(lectorId).stream()
                .map(homeworkConverter::entityToDtoForLector).toList();
    }

    @Transactional(readOnly = true)
    public HomeworkDto findByLectureIdAndStudentId(long lectureId, long studentId) {
        return homeworkRepository.findByLectureIdAndStudentAccountId(lectureId, studentId)
                .map(homeworkConverter::entityToBaseInfoDto)
                .orElseThrow(() -> new RuntimeException("Домашняя работа не найдена"));
    }

    @Transactional(readOnly = true)
    public List<HomeworkDto> findByLectureIdAndLectorId(long lectureId, long lectorId) {
        return homeworkRepository.findAllByLectureIdAndLectorId(lectureId, lectorId)
                .stream().map(homeworkConverter::entityToWithStudentDto).toList();
    }

    @Transactional
    public void createHomeworksForNewStudent(Student student, StudyGroup studyGroup) {
        List<Lecture> lectures = lectureRepository.findAllByStudyGroupId(studyGroup.getId());
        List<Lector> lectors = lectorRepository.findAllByCoursesId(studyGroup.getCourse().getId());
        List<Homework> homeworks = lectures.stream().map(lecture -> new Homework(0L, 0,
                statusConverter.enumToEntity(HomeworkStatusEnum.NOT_SEND),
                lecture, student, RandomUtils.getOneRandomFrom(lectors),
                new ArrayList<>()
        )).toList();
        homeworkRepository.saveAll(homeworks);
    }
}
