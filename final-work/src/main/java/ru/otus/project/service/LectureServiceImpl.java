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
import ru.otus.project.exception.LectureNotFoundException;
import ru.otus.project.repository.LectorRepository;
import ru.otus.project.repository.LectureRepository;
import ru.otus.project.utils.RandomUtils;

import java.util.ArrayList;
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
    public void createDefaultLecturesForGroup(StudyGroup studyGroup) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(studyGroup.getStartDate());

        List<Lector> lectors = lectorRepository.findAllByCoursesId(studyGroup.getCourse().getId());

        List<Lecture> lectures = studyGroup.getCourse().getLectureTopics().stream().map(lectureTopic -> {
            Lecture newLecture = new Lecture(0L, calendar.getTime(),
                    statusConverter.enumToEntity(LectureStatusEnum.IN_FUTURE),
                    lectureTopic,
                    RandomUtils.getOneRandomFrom(lectors),
                    studyGroup,
                    new ArrayList<>()
            );
            calendar.add(Calendar.DATE, 2);
            return newLecture;
        }).toList();
        lectureRepository.saveAll(lectures);
    }

    @Transactional
    public void removeLectorForCourseLectures(Course course, Lector oldLector) {
        List<Lecture> lectures = lectureRepository.findAllByLectorAccountIdAndCourseId(oldLector.getAccount().getId(), course.getId());
        List<Lecture> updatedLectures = lectures.stream().map(lecture ->
                new Lecture(lecture.getId(), lecture.getLectureTime(),
                        lecture.getStatus(),
                        lecture.getLectureTopic(),
                        RandomUtils.getOneRandomFrom(course.getLectors()),
                        lecture.getStudyGroup(),
                        lecture.getMaterials())).toList();
        lectureRepository.saveAll(updatedLectures);
    }
}
