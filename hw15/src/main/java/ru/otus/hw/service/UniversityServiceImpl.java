package ru.otus.hw.service;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Adult;
import ru.otus.hw.domain.Teenager;

import java.util.List;

@Service
public class UniversityServiceImpl implements UniversityService {

    private static final List<String> EDUCATION_DIPLOMAS = List.of(
            "Неоконченное",
            "Степень бакалавра",
            "Степень магистра",
            "Степень специалиста",
            "Доктор наук"
    );

    @Override
    public Adult awardScientificDegree(Teenager teenager) {

        String diploma = EDUCATION_DIPLOMAS.get(
                RandomUtils.secure().randomInt(0, EDUCATION_DIPLOMAS.size())
        );

        return new Adult(teenager.getName(),
                teenager.getBaseSkills(),
                teenager.getSchoolSubjects(),
                diploma
        );
    }
}
