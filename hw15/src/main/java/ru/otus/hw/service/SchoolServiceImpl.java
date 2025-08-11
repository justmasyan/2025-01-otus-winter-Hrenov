package ru.otus.hw.service;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Prescholler;
import ru.otus.hw.domain.Teenager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class SchoolServiceImpl implements SchoolService {

    private static final List<String> SCHOOL_SUBJECTS = List.of(
            "Математика",
            "Физика",
            "Русский язык",
            "Иностранный язык",
            "Физкультура",
            "История",
            "Биология",
            "Химия"
    );

    @Override
    public Teenager techBaseSciences(Prescholler prescholler) {
        Set<String> learnedSchoolSubjects = new TreeSet<>();

        for (int i = 0; i < 3; i++) {
            String skill = SCHOOL_SUBJECTS.get(
                    RandomUtils.secure().randomInt(0, SCHOOL_SUBJECTS.size())
            );
            learnedSchoolSubjects.add(skill);
        }

        return new Teenager(prescholler.getName(),
                prescholler.getBaseSkills(),
                new ArrayList<>(learnedSchoolSubjects)
        );
    }
}
