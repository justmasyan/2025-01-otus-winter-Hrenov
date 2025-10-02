package ru.otus.project.flyway;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlywayRunner implements CommandLineRunner {

    private final Flyway flyway;

    @Override
    public void run(String... args) {
        flyway.migrate();
    }
}
