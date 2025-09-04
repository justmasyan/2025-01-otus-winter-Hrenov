package ru.otus.hw.service;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Baby;
import ru.otus.hw.domain.Prescholler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class KindergartenServiceImpl implements KindergartenService {

    private static final List<String> KINDERGARTEN_BASE_SKILLS = List.of(
            "Ходить",
            "Разговаривать",
            "Дружить",
            "Познавать"
    );

    @Override
    public Prescholler teachBaseSkills(Baby baby) {
        Set<String> learnedSkills = new TreeSet<>();

        for (int i = 0; i < 3; i++) {
            String skill = KINDERGARTEN_BASE_SKILLS.get(
                    RandomUtils.secure().randomInt(0, KINDERGARTEN_BASE_SKILLS.size())
            );
            learnedSkills.add(skill);
        }

        return new Prescholler(baby.getName(), new ArrayList<>(learnedSkills));
    }
}
