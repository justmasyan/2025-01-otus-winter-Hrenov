package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.project.converter.HomeworkStatusConverter;
import ru.otus.project.dto.HomeworkStatusDto;
import ru.otus.project.repository.HomeworkStatusRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeworkStatusServiceImpl {

    private final HomeworkStatusRepository statusRepository;

    private final HomeworkStatusConverter statusConverter;

    public List<HomeworkStatusDto> findAll() {
        return statusRepository.findAll().stream()
                .map(statusConverter::entityToDto).toList();
    }
}
