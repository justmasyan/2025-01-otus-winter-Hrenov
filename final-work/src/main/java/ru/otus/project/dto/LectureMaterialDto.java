package ru.otus.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureMaterialDto {

    private long id;

    private String url;

    private LectureDto lecture;

    public LectureMaterialDto(long id, String url) {
        this.id = id;
        this.url = url;
    }
}
