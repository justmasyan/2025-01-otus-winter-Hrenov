package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.converter.CourseConverter;
import ru.otus.project.dto.CourseDto;
import ru.otus.project.repository.CourseRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    private final CourseConverter courseConverter;

    @Override
    @Transactional(readOnly = true)
    public CourseDto findById(long id) {
        return courseRepository.findById(id).map(courseConverter::entityToDto)
                .orElseThrow(() -> new RuntimeException("exception"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> findAllBaseInfo() {
        return courseRepository.findAll().stream()
                .map(courseConverter::entityToBaseInfoDto).toList();
    }
}
