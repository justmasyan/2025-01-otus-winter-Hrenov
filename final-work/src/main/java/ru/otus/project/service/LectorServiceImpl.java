package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.converter.LectorConverter;
import ru.otus.project.domain.Account;
import ru.otus.project.domain.Course;
import ru.otus.project.domain.Lector;
import ru.otus.project.dto.AccountDto;
import ru.otus.project.dto.LectorDto;
import ru.otus.project.repository.CourseRepository;
import ru.otus.project.repository.LectorRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LectorServiceImpl {

    private final AccountServiceImpl accountService;

    private final LectureServiceImpl lectureService;

    private final LectorRepository lectorRepository;

    private final CourseRepository courseRepository;

    private final LectorConverter lectorConverter;

    @Transactional(readOnly = true)
    public LectorDto findByAccountId(long accountId) {
        return lectorRepository.findByAccountId(accountId).map(lectorConverter::entityToDto)
                .orElseThrow(() -> new RuntimeException("Lector not found"));
    }

    @Transactional(readOnly = true)
    public List<LectorDto> findAllByCourseId(long courseId) {
        return lectorRepository.findAllByCoursesId(courseId).stream()
                .map(lectorConverter::entityToBaseInfoDto).toList();
    }

    @Transactional
    public LectorDto insert(LectorDto lectorDto) {
        if (lectorDto.getId() != 0 || lectorDto.getAccount().getId() != 0) {
            throw new RuntimeException("Id не должен равняться нулю");
        }
        return save(lectorDto);
    }

    @Transactional
    public LectorDto moveLectorToCourse(long accountId, long courseId) {
        Lector lector = lectorRepository.findByAccountId(accountId)
                .orElseThrow(() -> new RuntimeException("Лектора с таким accountId не существует"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Такого курса не существует"));

        if (lector.getCourses().contains(course)) {
            throw new RuntimeException("Вы уже преподаете на этом курсе");
        }

        lector.getCourses().add(course);
        Lector savedLector = lectorRepository.save(lector);
        return lectorConverter.entityToDto(savedLector);
    }

    @Transactional
    public LectorDto deleteLectorFromCourse(long accountId, long courseId) {
        List<Lector> courseLectors = lectorRepository.findAllByCoursesId(courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Такого курса не существует"));

        if (courseLectors.size() <= 1) {
            courseRepository.delete(course);
            Lector lector = lectorRepository.findByAccountId(accountId)
                    .orElseThrow(() -> new RuntimeException("Такого преподавателя не существует"));
            return lectorConverter.entityToDto(lector);
        }

        courseLectors.removeIf(courseLector -> courseLector.getAccount().getId() == accountId);
        Lector lector = courseLectors.stream().filter(courseLector -> courseLector.getAccount().getId() == accountId)
                .findFirst().orElseThrow(() -> new RuntimeException("Лектора с таким accountId не существует"));


        if (!lector.getCourses().contains(course)) {
            throw new RuntimeException("Вы не преподаете на этом курсе");
        }

        lector.getCourses().remove(course);
        Lector savedLector = lectorRepository.save(lector);

        lectureService.changeLecturesForLector(savedLector.getAccount().getId(), courseId);
        return lectorConverter.entityToDto(savedLector);
    }

    private LectorDto save(LectorDto lectorDto) {
        AccountDto account = accountService.insert(lectorDto.getAccount());
        Lector lector = lectorRepository.save(new Lector(
                lectorDto.getId(),
                lectorDto.getFirstName(),
                lectorDto.getLastName(),
                lectorDto.getJobTitle(),
                new Account(account.getId()),
                new ArrayList<>()
        ));
        return lectorConverter.entityToDto(lector);
    }
}
