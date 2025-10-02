package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.converter.StudyGroupConverter;
import ru.otus.project.domain.Course;
import ru.otus.project.domain.Student;
import ru.otus.project.domain.StudyGroup;
import ru.otus.project.dto.StudyGroupDto;
import ru.otus.project.exception.CourseNotFoundException;
import ru.otus.project.exception.NoZeroIdException;
import ru.otus.project.exception.StudentAlreadyInGroupException;
import ru.otus.project.exception.StudentNotFoundException;
import ru.otus.project.exception.StudentNotInGroupException;
import ru.otus.project.exception.StudentsExistWhenGroupCreateException;
import ru.otus.project.exception.StudyGroupNotFoundException;
import ru.otus.project.repository.CourseRepository;
import ru.otus.project.repository.StudentRepository;
import ru.otus.project.repository.StudyGroupRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StudyGroupServiceImpl {

    private final LectureServiceImpl lectureService;

    private final HomeworkServiceImpl homeworkService;

    private final StudyGroupRepository studyGroupRepository;

    private final StudentRepository studentRepository;

    private final CourseRepository courseRepository;

    private final StudyGroupConverter studyGroupConverter;

    @Transactional(readOnly = true)
    public StudyGroupDto findById(long id) {
        return studyGroupRepository.findById(id)
                .map(studyGroupConverter::entityWithCourseDto)
                .orElseThrow(() -> new StudyGroupNotFoundException("Учебная группа не найдена"));
    }

    @Transactional(readOnly = true)
    public List<StudyGroupDto> findAllByStudentAccountId(long accountId) {
        return studyGroupRepository.findAllByStudentsAccountId(accountId).stream()
                .map(studyGroupConverter::entityToBaseInfoDto).toList();
    }

    @Transactional(readOnly = true)
    public List<StudyGroupDto> findAllByCourseId(long courseId) {
        return studyGroupRepository.findAllByCourseId(courseId).stream()
                .map(studyGroupConverter::entityToBaseInfoDto).toList();
    }

    @Transactional
    public StudyGroupDto insertGroup(StudyGroupDto studyGroupDto) {
        if (studyGroupDto.getId() != 0) {
            throw new NoZeroIdException("Id должен равняться нулю");
        }

        if (!Objects.isNull(studyGroupDto.getStudents())) {
            throw new StudentsExistWhenGroupCreateException("При создании группы не должно быть студентов");
        }

        Course course = courseRepository.findById(studyGroupDto.getCourse().getId())
                .orElseThrow(() -> new CourseNotFoundException("Курс не найден"));

        StudyGroup studyGroup = new StudyGroup(studyGroupDto.getId(),
                studyGroupDto.getCode(), studyGroupDto.getStartDate(),
                course, new ArrayList<>());

        StudyGroup savedGroup = studyGroupRepository.save(studyGroup);
        lectureService.createDefaultLecturesForGroup(savedGroup);
        return studyGroupConverter.entityWithCourseDto(savedGroup);
    }

    @Transactional
    public StudyGroupDto addStudentToGroup(long groupId, long studentAccountId) {
        Student student = studentRepository.findByAccountId(studentAccountId)
                .orElseThrow(() -> new StudentNotFoundException("Студент не найден"));

        StudyGroup group = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new StudyGroupNotFoundException("Группа не найдена"));

        if (group.getStudents().contains(student)) {
            throw new StudentAlreadyInGroupException("Студент уже записан");
        }

        group.getStudents().add(student);
        StudyGroup studyGroup = studyGroupRepository.save(group);
        homeworkService.createHomeworksForNewStudent(student, studyGroup);
        return studyGroupConverter.entityToBaseInfoDto(studyGroup);
    }

    @Transactional
    public StudyGroupDto removeStudentFromGroup(long groupId, long studentAccountId) {
        Student student = studentRepository.findByAccountId(studentAccountId)
                .orElseThrow(() -> new StudentNotFoundException("Студент не найден"));

        StudyGroup group = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new StudyGroupNotFoundException("Группа не найдена"));

        if (!group.getStudents().contains(student)) {
            throw new StudentNotInGroupException("Студента нет в этой группу");
        }

        group.getStudents().remove(student);
        StudyGroup studyGroup = studyGroupRepository.save(group);
        homeworkService.deleteHomeworksForStudentAndGroup(student, studyGroup);
        return studyGroupConverter.entityToBaseInfoDto(studyGroup);
    }
}
