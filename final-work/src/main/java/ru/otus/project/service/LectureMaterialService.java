package ru.otus.project.service;

import ru.otus.project.dto.LectureMaterialDto;

import java.util.List;

public interface LectureMaterialService {

    List<LectureMaterialDto> findAllByLectureId(long lectureId);

    LectureMaterialDto insertMaterial(LectureMaterialDto materialDto, long lectorAccountId);

    void deleteMaterial(long materialId, long lectorAccountId);
}
