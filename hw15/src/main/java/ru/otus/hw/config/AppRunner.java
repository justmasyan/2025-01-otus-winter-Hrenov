package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.service.StudyService;

@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    private final StudyService studyService;

    @Override
    public void run(String... args) throws Exception {
        studyService.startTeachBabyLoop();
    }
}
