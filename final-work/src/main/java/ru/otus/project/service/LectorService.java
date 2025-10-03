package ru.otus.project.service;

import ru.otus.project.dto.LectorDto;

import java.util.List;

public interface LectorService {

    LectorDto findByAccountId(long accountId);

    List<LectorDto> findAllByCourseId(long courseId);

    LectorDto insert(LectorDto lectorDto);

    LectorDto update(LectorDto lectorDto);
}
