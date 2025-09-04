package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Adult;
import ru.otus.hw.domain.Baby;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {

    private static final List<String> LIST_OF_NAMES = List.of(
            "PETYA",
            "MASHA",
            "SVETA"
    );

    private final StudyGateway studyGateway;

    private Integer countBaby = 0;

    @Override
    public void startTeachBabyLoop() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        for (int i = 0; i < 10; i++) {
            pool.execute(() -> {
                List<Baby> babies = getNewBorn();
                Collection<Adult> adults = studyGateway.process(babies);

                adults.forEach(adult -> log.info("Имя обучающегося: {}," +
                                " Базовые навыки ({})," +
                                " Изученные дисциплины ({})," +
                                " Полученная степень: {}",
                        adult.getName(),
                        String.join(", ", adult.getBaseSkills()),
                        String.join(", ", adult.getSchoolSubjects()),
                        adult.getEducationDiploma()));
            });
            delay();
        }
    }

    private List<Baby> getNewBorn() {
        int numberOfBaby = RandomUtils.secure().randomInt(0, 10);
        List<Baby> babies = new ArrayList<>(numberOfBaby);
        for (int i = 0; i < numberOfBaby; i++) {
            String babyName = LIST_OF_NAMES.get(RandomUtils.secure().randomInt(0, LIST_OF_NAMES.size()));
            babies.add(new Baby(babyName));
        }
        countBaby += numberOfBaby;

        return babies;
    }

    private void delay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
