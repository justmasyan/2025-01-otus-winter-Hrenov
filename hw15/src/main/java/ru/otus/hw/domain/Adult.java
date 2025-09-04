package ru.otus.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Adult {

    private String name;

    private List<String> baseSkills;

    private List<String> schoolSubjects;

    private String educationDiploma;
}
