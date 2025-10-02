package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.otus.project.converter.LectureStatusConverter;
import ru.otus.project.dto.LectureStatusDto;
import ru.otus.project.repository.LectureStatusRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureStatusServiceImpl {

    private final LectureStatusRepository statusRepository;

    private final LectureStatusConverter statusConverter;

    public List<LectureStatusDto> findAll() {
        return statusRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(statusConverter::entityToDto).toList();
    }
}
