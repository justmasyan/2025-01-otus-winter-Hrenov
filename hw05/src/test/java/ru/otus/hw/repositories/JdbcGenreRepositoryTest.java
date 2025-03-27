package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с жанрами ")
@JdbcTest
@Import({JdbcGenreRepository.class})
class JdbcGenreRepositoryTest {

    @Autowired
    private JdbcGenreRepository genreRepository;

    private List<Genre> genres;

    @BeforeEach
    void setUp() {
        genres = getDbGenres();
    }

    @DisplayName("Должен вывести все имеющиеся жанры")
    @Test
    void shouldReturnCorrectGenreList() {
        assertThat(genres).isEqualTo(genreRepository.findAll());
    }

    @DisplayName("Должен вывести несколько жанров по нескольким id")
    @Test
    void shouldReturnCorrectGenreListByIds() {
        Set<Long> expectedIds = Set.of(1L, 3L, 6L);
        List<Genre> expectedGenres = genres.stream().filter(genre -> expectedIds.contains(genre.getId())).toList();
        assertThat(genreRepository.findAllByIds(expectedIds)).isEqualTo(expectedGenres);
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }
}