package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.converter.LectureConverter;
import ru.otus.project.converter.LectureStatusConverter;
import ru.otus.project.domain.Course;
import ru.otus.project.domain.Lector;
import ru.otus.project.domain.Lecture;
import ru.otus.project.domain.StudyGroup;
import ru.otus.project.dto.LectureDto;
import ru.otus.project.enums.LectureStatusEnum;
import ru.otus.project.exception.LectorAlreadyInLectureException;
import ru.otus.project.exception.LectorNotFoundException;
import ru.otus.project.exception.LectorNotInCourseException;
import ru.otus.project.exception.LectorNotInLectureException;
import ru.otus.project.exception.LectureNotFoundException;
import ru.otus.project.exception.NoAvailableLectorsException;
import ru.otus.project.repository.LectorRepository;
import ru.otus.project.repository.LectureRepository;
import ru.otus.project.utils.RandomUtils;

import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectorRepository lectorRepository;

    private final LectureRepository lectureRepository;

    private final LectureConverter lectureConverter;

    private final LectureStatusConverter statusConverter;

    @Transactional(readOnly = true)
    public LectureDto findById(long id) {
        return lectureRepository.findById(id)
                .map(lectureConverter::entityToFullDto)
                .orElseThrow(() -> new LectureNotFoundException("Лекция не существует"));
    }

    @Transactional(readOnly = true)
    public List<LectureDto> findAllByGroupId(long lectorId) {
        return lectureRepository.findAllByStudyGroupId(lectorId).stream()
                .map(lectureConverter::entityToWithStatusTopicDto).toList();
    }

    @Transactional(readOnly = true)
    public List<LectureDto> findAllByLectorAccountId(long lectorAccountId) {
        return lectureRepository.findAllByLectorAccountId(lectorAccountId).stream()
                .map(lectureConverter::entityToWithStatusTopicGroupDto).toList();
    }

    @Transactional
    public LectureDto updateLectureByAccount(LectureDto lectureDto, long accountId) {
        Lecture lecture = lectureRepository.findById(lectureDto.getId())
                .orElseThrow(() -> new LectureNotFoundException("Лекция не найдена"));

        if (lecture.getLector().getAccount().getId() != accountId) {
            throw new LectorNotInLectureException("Данный преподаватель не ведет эту лекцию");
        }

        Lecture newLecture = new Lecture(lecture.getId(),
                lectureDto.getLectureTime(),
                statusConverter.dtoToEntity(lectureDto.getStatus()),
                lecture.getLectureTopic(),
                lecture.getLector(),
                lecture.getStudyGroup());
        Lecture updateLecture = lectureRepository.save(newLecture);
        return lectureConverter.entityToFullDto(updateLecture);
    }

    @Transactional
    public void createDefaultLecturesForGroup(StudyGroup studyGroup) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(studyGroup.getStartDate());

        List<Lector> lectors = lectorRepository.findAllByCoursesId(studyGroup.getCourse().getId());

        List<Lecture> lectures = studyGroup.getCourse().getLectureTopics().stream().map(lectureTopic -> {
            Lecture newLecture = new Lecture(0L, calendar.getTime(),
                    statusConverter.enumToEntity(LectureStatusEnum.IN_FUTURE),
                    lectureTopic,
                    RandomUtils.getOneRandomFrom(lectors),
                    studyGroup
            );
            calendar.add(Calendar.DATE, 2);
            return newLecture;
        }).toList();
        lectureRepository.saveAll(lectures);
    }

    @Transactional
    public void assignLectorOnLecture(long lectureId, long lectorAccountId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("Лекция не найдена"));

        Lector lector = lectorRepository.findByAccountId(lectorAccountId)
                .orElseThrow(() -> new LectorNotFoundException("Лектор не найден"));

        if (lecture.getLector().getId() == lectorAccountId) {
            throw new LectorAlreadyInLectureException("Данный преподаватель уже ведет эту лекцию");
        }

        if (!lector.getCourses().contains(lecture.getStudyGroup().getCourse())) {
            throw new LectorNotInCourseException("Данного преподавателя нет на курсе");
        }

        Lecture newLecture = new Lecture(lecture.getId(),
                lecture.getLectureTime(),
                lecture.getStatus(),
                lecture.getLectureTopic(),
                lector,
                lecture.getStudyGroup());
        lectureRepository.save(newLecture);
    }

    @Transactional
    public void removeLectorFromLecture(long lectureId, long lectorAccountId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("Лекция с таким id не найдена"));

        if (lecture.getLector().getAccount().getId() != lectorAccountId) {
            throw new LectorNotInLectureException("Данный преподаватель не ведет эту лекцию");
        }

        long courseId = lecture.getStudyGroup().getCourse().getId();
        List<Lector> availableLectors = lectorRepository.findAllByCoursesId(courseId);
        availableLectors.remove(lecture.getLector());

        if (availableLectors.isEmpty()) {
            throw new NoAvailableLectorsException("Нет доступных преподавателей для лекции");
        }

        Lecture newLecture = new Lecture(lecture.getId(),
                lecture.getLectureTime(),
                lecture.getStatus(),
                lecture.getLectureTopic(),
                RandomUtils.getOneRandomFrom(availableLectors),
                lecture.getStudyGroup());
        lectureRepository.save(newLecture);
    }

    @Transactional
    public void removeLectorForCourseLectures(Course course, Lector oldLector) {
        List<Lecture> lectures = lectureRepository.findAllByLectorAccountIdAndCourseId(oldLector.getAccount().getId(), course.getId());
        List<Lecture> updatedLectures = lectures.stream().map(lecture ->
                new Lecture(lecture.getId(), lecture.getLectureTime(),
                        lecture.getStatus(),
                        lecture.getLectureTopic(),
                        RandomUtils.getOneRandomFrom(course.getLectors()),
                        lecture.getStudyGroup())).toList();
        lectureRepository.saveAll(updatedLectures);
    }
}
