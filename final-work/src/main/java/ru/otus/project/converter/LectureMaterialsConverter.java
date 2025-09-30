package ru.otus.project.converter;

import org.springframework.stereotype.Component;
import ru.otus.project.domain.LectureMaterial;
import ru.otus.project.dto.LectureMaterialDto;

@Component
public class LectureMaterialsConverter {

    public LectureMaterialDto entityToDto(LectureMaterial materials) {
        return new LectureMaterialDto(materials.getId(), materials.getUrl());
    }
}
