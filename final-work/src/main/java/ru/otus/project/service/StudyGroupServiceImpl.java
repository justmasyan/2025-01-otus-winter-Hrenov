package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.converter.StudyGroupConverter;
import ru.otus.project.domain.Student;
import ru.otus.project.domain.StudyGroup;
import ru.otus.project.dto.StudyGroupDto;
import ru.otus.project.repository.StudentRepository;
import ru.otus.project.repository.StudyGroupRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyGroupServiceImpl {

    private final HomeworkServiceImpl homeworkService;

    private final StudyGroupRepository studyGroupRepository;

    private final StudyGroupConverter studyGroupConverter;

    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public StudyGroupDto findById(long id) {
        return studyGroupRepository.findById(id)
                .map(studyGroupConverter::entityWithCourseDto)
                .orElseThrow(() -> new RuntimeException("exception"));
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
    public StudyGroupDto addStudentToGroup(long groupId, long studentAccountId) {
        Student student = studentRepository.findByAccountId(studentAccountId)
                .orElseThrow(() -> new RuntimeException("Студент не найден"));

        StudyGroup group = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));

        if (group.getStudents().contains(student)) {
            throw new RuntimeException("Студент уже записан");
        }

        group.getStudents().add(student);
        StudyGroup studyGroup = studyGroupRepository.save(group);
        homeworkService.createHomeworksForNewStudent(student, studyGroup);
        return studyGroupConverter.entityToBaseInfoDto(studyGroup);
    }
}
