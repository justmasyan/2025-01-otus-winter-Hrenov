package ru.otus.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Prescholler {

    private String name;

    private List<String> baseSkills;
}
