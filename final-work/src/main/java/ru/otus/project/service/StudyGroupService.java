package ru.otus.project.service;

import ru.otus.project.dto.StudyGroupDto;

import java.util.List;

public interface StudyGroupService {

    StudyGroupDto findById(long id);

    List<StudyGroupDto> findAllByStudentAccountId(long accountId);

    List<StudyGroupDto> findAllByCourseId(long courseId);

    StudyGroupDto insertGroup(StudyGroupDto studyGroupDto, long lectorAccountId);

    StudyGroupDto addStudentToGroup(long groupId, long studentAccountId);

    StudyGroupDto removeStudentFromGroup(long groupId, long studentAccountId);
}
