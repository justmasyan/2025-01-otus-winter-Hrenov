package ru.otus.hw.service;

import ru.otus.hw.domain.Adult;
import ru.otus.hw.domain.Teenager;

public interface UniversityService {
    Adult awardScientificDegree(Teenager teenager);
}
