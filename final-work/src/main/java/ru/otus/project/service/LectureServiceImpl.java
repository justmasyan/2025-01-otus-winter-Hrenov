package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.converter.LectureConverter;
import ru.otus.project.domain.Lector;
import ru.otus.project.domain.Lecture;
import ru.otus.project.dto.LectureDto;
import ru.otus.project.repository.LectorRepository;
import ru.otus.project.repository.LectureRepository;
import ru.otus.project.utils.RandomUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;

    private final LectorRepository lectorRepository;

    private final LectureConverter lectureConverter;

    @Transactional(readOnly = true)
    public LectureDto findById(long id) {
        return lectureRepository.findById(id)
                .map(lectureConverter::entityToFullDto)
                .orElseThrow(() -> new RuntimeException("Нет лекции"));
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
    public void changeLecturesForLector(long lectorAccountId, long courseId) {
        List<Lecture> lectures = lectureRepository.findAllByLectorAccountIdAndStudyGroupCourseId(lectorAccountId, courseId);
        List<Lector> lectors = lectorRepository.findAllByCoursesId(courseId);

        List<Lecture> updatedLectures = lectures.stream().map(lecture ->
                new Lecture(lecture.getId(), lecture.getLectureTime(),
                        lecture.getStatus(), lecture.getLectureTopic(),
                        RandomUtils.getOneRandomFrom(lectors),
                        lecture.getStudyGroup(),
                        lecture.getMaterials())
        ).toList();
        lectureRepository.saveAll(updatedLectures);
    }
}
