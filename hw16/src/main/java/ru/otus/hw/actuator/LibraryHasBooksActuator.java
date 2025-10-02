package ru.otus.hw.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.BookRepository;

@Component
@RequiredArgsConstructor
public class LibraryHasBooksActuator implements HealthIndicator {

    private final BookRepository bookRepository;

    @Override
    public Health health() {
        long bookCount = bookRepository.count();
        return (bookCount > 0) ? Health.up().withDetail("message", "В библиотеке есть книги").build() :
                Health.down().withDetail("message", "В библиотеке отсутствуют книги").build();
    }
}
