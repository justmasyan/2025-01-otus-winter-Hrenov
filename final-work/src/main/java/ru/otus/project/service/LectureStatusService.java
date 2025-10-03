package ru.otus.project.service;

import ru.otus.project.dto.LectureStatusDto;

import java.util.List;


public interface LectureStatusService {

    List<LectureStatusDto> findAll();
}
