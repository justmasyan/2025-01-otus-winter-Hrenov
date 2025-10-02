package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.converter.LectureMaterialsConverter;
import ru.otus.project.domain.Lecture;
import ru.otus.project.domain.LectureMaterial;
import ru.otus.project.dto.LectureMaterialDto;
import ru.otus.project.exception.EmptyLectureException;
import ru.otus.project.exception.LectorNotInLectureException;
import ru.otus.project.exception.LectureNotFoundException;
import ru.otus.project.exception.NoZeroIdException;
import ru.otus.project.repository.LectureMaterialRepository;
import ru.otus.project.repository.LectureRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LectureMaterialServiceImpl {

    private final LectureRepository lectureRepository;

    private final LectureMaterialRepository materialRepository;

    private final LectureMaterialsConverter materialsConverter;

    @Transactional(readOnly = true)
    public List<LectureMaterialDto> findAllByLectureId(long lectureId) {
        return materialRepository.findAllByLectureId(lectureId).stream()
                .map(materialsConverter::entityToMinDto).toList();
    }

    @Transactional
    public LectureMaterialDto insertMaterial(LectureMaterialDto materialDto, long lectorAccountId) {
        if (materialDto.getId() != 0) {
            throw new NoZeroIdException("id должен равняться 0");
        }

        if (Objects.isNull(materialDto.getLecture()) || materialDto.getLecture().getId() == 0) {
            throw new EmptyLectureException("Лекция не должна быть пустой");
        }

        Lecture lecture = lectureRepository.findById(materialDto.getLecture().getId())
                .orElseThrow(() -> new LectureNotFoundException("Лекция не найдена"));

        if (lecture.getLector().getAccount().getId() != lectorAccountId) {
            throw new LectorNotInLectureException("Данный преподаватель не ведет эту лекцию");
        }

        LectureMaterial material = new LectureMaterial(
                materialDto.getId(),
                materialDto.getUrl(),
                lecture);

        LectureMaterial savedMaterial = materialRepository.save(material);
        return materialsConverter.entityToMinDto(savedMaterial);
    }

    @Transactional
    public void deleteMaterial(long materialId) {
        materialRepository.deleteById(materialId);
    }
}
