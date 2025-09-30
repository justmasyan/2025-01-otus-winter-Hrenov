package ru.otus.project.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.project.domain.Lector;
import ru.otus.project.dto.LectorDto;

@Component
@RequiredArgsConstructor
public class LectorConverter {

    private final AccountConverter accountConverter;

    private final CourseConverter courseConverter;

    public LectorDto entityToBaseInfoDto(Lector lector) {
        return new LectorDto(lector.getId(), lector.getFirstName(),
                lector.getLastName(), lector.getJobTitle());
    }

    public LectorDto entityToDto(Lector lector) {
        return new LectorDto(lector.getId(), lector.getFirstName(),
                lector.getLastName(), lector.getJobTitle(),
                accountConverter.entityToBaseInfoDto(lector.getAccount()),
                lector.getCourses().stream().map(courseConverter::entityToMinDto).toList());
    }
}
