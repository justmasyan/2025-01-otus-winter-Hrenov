package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.converter.HomeworkConverter;
import ru.otus.project.converter.HomeworkStatusConverter;
import ru.otus.project.domain.Course;
import ru.otus.project.domain.Homework;
import ru.otus.project.domain.HomeworkStatus;
import ru.otus.project.domain.Lector;
import ru.otus.project.domain.Lecture;
import ru.otus.project.domain.Student;
import ru.otus.project.domain.StudyGroup;
import ru.otus.project.dto.HomeworkDto;
import ru.otus.project.enums.HomeworkStatusEnum;
import ru.otus.project.exception.HomeworkNotFoundException;
import ru.otus.project.exception.HomeworkStatusNotFoundException;
import ru.otus.project.exception.LectorAlreadyInHomeworkException;
import ru.otus.project.exception.LectorNotFoundException;
import ru.otus.project.exception.LectorNotInCourseException;
import ru.otus.project.exception.LectorNotInHomeworkException;
import ru.otus.project.exception.NoAvailableLectorsException;
import ru.otus.project.repository.HomeworkRepository;
import ru.otus.project.repository.HomeworkStatusRepository;
import ru.otus.project.repository.LectorRepository;
import ru.otus.project.repository.LectureRepository;
import ru.otus.project.utils.RandomUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeworkServiceImpl {

    private final LectureRepository lectureRepository;

    private final LectorRepository lectorRepository;

    private final HomeworkRepository homeworkRepository;

    private final HomeworkStatusRepository statusRepository;

    private final HomeworkConverter homeworkConverter;

    private final HomeworkStatusConverter statusConverter;

    @Transactional(readOnly = true)
    public HomeworkDto findById(long id) {
        return homeworkRepository.findById(id)
                .map(homeworkConverter::entityToDto)
                .orElseThrow(() -> new HomeworkNotFoundException("Домашняя работа не найдена"));
    }

    @Transactional(readOnly = true)
    public List<HomeworkDto> findAllByLectorId(long lectorAccountId) {
        return homeworkRepository.findAllByLectorAccountId(lectorAccountId).stream()
                .map(homeworkConverter::entityToDtoForLector).toList();
    }

    @Transactional(readOnly = true)
    public List<HomeworkDto> findAllByLectureId(long lectureId) {
        return homeworkRepository.findAllByLectureId(lectureId).stream()
                .map(homeworkConverter::entityToDtoForLecture).toList();
    }

    @Transactional(readOnly = true)
    public HomeworkDto findByLectureIdAndStudentId(long lectureId, long studentId) {
        return homeworkRepository.findByLectureIdAndStudentAccountId(lectureId, studentId)
                .map(homeworkConverter::entityToBaseInfoDto)
                .orElseThrow(() -> new HomeworkNotFoundException("Домашняя работа не найдена"));
    }

    @Transactional(readOnly = true)
    public List<HomeworkDto> findByLectureIdAndLectorId(long lectureId, long lectorId) {
        return homeworkRepository.findAllByLectureIdAndLectorId(lectureId, lectorId)
                .stream().map(homeworkConverter::entityToWithStudentDto).toList();
    }

    @Transactional
    public HomeworkDto updateHomework(HomeworkDto homeworkDto, long accountId){
        Homework homework = homeworkRepository.findById(homeworkDto.getId())
                .orElseThrow(() -> new HomeworkNotFoundException("Домашняя работа не найдена"));

        if(homework.getLector().getAccount().getId() != accountId){
            throw new LectorNotInHomeworkException("Данный лектор не проверяет это ДЗ");
        }

        HomeworkStatus status = statusRepository.findById(homeworkDto.getStatus().getId())
                .orElseThrow(() -> new HomeworkStatusNotFoundException("Данный статус ДЗ не найден"));

        Homework newHomework = new Homework(homework.getId(),
                homeworkDto.getMark(),
                status,
                homework.getLecture(),
                homework.getStudent(),
                homework.getLector());
        Homework savedHomework = homeworkRepository.save(newHomework);
        return homeworkConverter.entityToDto(savedHomework);
    }

    @Transactional
    public void createHomeworksForNewStudent(Student student, StudyGroup studyGroup) {
        List<Lecture> lectures = lectureRepository.findAllByStudyGroupId(studyGroup.getId());
        List<Lector> lectors = lectorRepository.findAllByCoursesId(studyGroup.getCourse().getId());
        List<Homework> homeworks = lectures.stream().map(lecture -> new Homework(0L, 0,
                statusConverter.enumToEntity(HomeworkStatusEnum.NOT_SEND),
                lecture, student, RandomUtils.getOneRandomFrom(lectors)
        )).toList();
        homeworkRepository.saveAll(homeworks);
    }

    @Transactional
    public void deleteHomeworksForStudentAndGroup(Student student, StudyGroup studyGroup) {
        List<Homework> homeworks = homeworkRepository.findAllByStudentAndStudyGroup(student.getId(), studyGroup.getId());
        homeworkRepository.deleteAllInBatch(homeworks);
    }

    @Transactional
    public void removeLectorForCourseHomeworks(Course course, Lector oldLector) {
        List<Homework> homeworks = homeworkRepository.findAllByLectorAndCourse(oldLector.getId(), course.getId());
        List<Homework> updatedHomeworks = homeworks.stream().map(homework ->
                new Homework(homework.getId(), homework.getMark(),
                        homework.getStatus(),
                        homework.getLecture(),
                        homework.getStudent(),
                        RandomUtils.getOneRandomFrom(course.getLectors()))
        ).toList();
        homeworkRepository.saveAll(updatedHomeworks);
    }

    @Transactional
    public void assignLectorToHomework(long homeworkId, long lectorAccountId) {
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new HomeworkNotFoundException("Домашняя работа не найдена"));

        if (homework.getLector().getAccount().getId() == lectorAccountId) {
            throw new LectorAlreadyInHomeworkException("Данный преподаватель уже назначен проверяющим");
        }

        Lector lector = lectorRepository.findByAccountId(lectorAccountId)
                .orElseThrow(() -> new LectorNotFoundException("Лектор не найден"));

        if (!lector.getCourses().contains(homework.getLecture().getStudyGroup().getCourse())) {
            throw new LectorNotInCourseException("Данного преподавателя нет на курсе");
        }

        Homework newHomework = new Homework(homework.getId(),
                homework.getMark(),
                homework.getStatus(),
                homework.getLecture(),
                homework.getStudent(),
                lector);

        homeworkRepository.save(newHomework);
    }

    @Transactional
    public void removeLectorFromHomework(long homeworkId, long lectorAccountId) {
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new HomeworkNotFoundException("Лекция с таким id не найдена"));

        if (homework.getLector().getAccount().getId() != lectorAccountId) {
            throw new LectorNotInHomeworkException("Данный преподаватель не ведет эту лекцию");
        }

        long courseId = homework.getLecture().getStudyGroup().getCourse().getId();
        List<Lector> availableLectors = lectorRepository.findAllByCoursesId(courseId);
        availableLectors.remove(homework.getLector());

        if (availableLectors.isEmpty()) {
            throw new NoAvailableLectorsException("Нет доступных преподавателей");
        }

        Homework newHomework = new Homework(homework.getId(),
                homework.getMark(),
                homework.getStatus(),
                homework.getLecture(),
                homework.getStudent(),
                RandomUtils.getOneRandomFrom(availableLectors));
        homeworkRepository.save(newHomework);
    }
}
