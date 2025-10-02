package ru.otus.hw.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.AuthorRepository;

@Component
@RequiredArgsConstructor
public class LibraryHasAuthorsActuator implements HealthIndicator {

    private final AuthorRepository authorRepository;

    @Override
    public Health health() {
        long authorCount = authorRepository.count();
        return (authorCount > 0) ? Health.up().withDetail("message", "В библиотеке есть жанры").build() :
                Health.down().withDetail("message", "В библиотеке отсутствуют жанры").build();
    }
}
