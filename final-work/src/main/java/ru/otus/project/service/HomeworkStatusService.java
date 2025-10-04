package ru.otus.project.service;

import ru.otus.project.dto.HomeworkStatusDto;

import java.util.List;

public interface HomeworkStatusService {

    List<HomeworkStatusDto> findAll();
}
