package ru.otus.hw.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.GenreRepository;

@Component
@RequiredArgsConstructor
public class LibraryHasGenresActuator implements HealthIndicator {

    private final GenreRepository genreRepository;

    @Override
    public Health health() {
        long genreCount = genreRepository.count();
        return (genreCount > 0) ? Health.up().withDetail("message", "В библиотеке есть жанры").build() :
                Health.down().withDetail("message", "В библиотеке отсутствуют жанры").build();
    }
}
