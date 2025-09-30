package ru.otus.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LectureMaterialDto {

    private long id;

    private String url;
}
