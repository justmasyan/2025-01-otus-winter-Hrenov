package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentaryConverter;
import ru.otus.hw.entities.CommentaryDto;
import ru.otus.hw.models.Commentary;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentaryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({CommentaryServiceImpl.class, CommentaryConverter.class,
        JpaCommentaryRepository.class, JpaBookRepository.class})
@Transactional(propagation = Propagation.NESTED)
class CommentaryServiceImplTest {

    @Autowired
    private CommentaryServiceImpl commentaryService;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Результат Метода findById не должен содержать Lazy полей")
    @ParameterizedTest
    @MethodSource("getDbCommentaries")
    void resultShouldNoHaveLazyFieldsFindById(CommentaryDto expectedDto) {
        Optional<Integer> actualHashCode = commentaryService.findById(expectedDto.getId())
                .map(CommentaryDto::hashCode);

        assertThat(actualHashCode).isPresent()
                .get().isEqualTo(expectedDto.hashCode());

    }

    @DisplayName("Результат Метода findAllByBookId не должен содержать Lazy полей")
    @Test
    void resultShouldNoHaveLazyFieldsFindByBookId() {
        long bookId = 1L;
        List<Integer> expectedHashCodes = Stream.of(
                new CommentaryDto(1L, 1L, "Comment_1"),
                new CommentaryDto(2L, 1L, "Comment_2")
        ).map(CommentaryDto::hashCode).toList();

        List<Integer> actualHashCodes = commentaryService.findAllByBookId(bookId).stream()
                .map(CommentaryDto::hashCode).toList();

        assertThat(actualHashCodes).isEqualTo(expectedHashCodes);
    }

    @DisplayName("Результат Метода insert не должен содержать Lazy полей")
    @Test
    void resultShouldNoHaveLazyFieldsInsert() {
        CommentaryDto expectedDto = new CommentaryDto(7, 1, "WOW");
        CommentaryDto actualDto = commentaryService.insert(expectedDto.getBookId(), expectedDto.getText());
        assertThat(actualDto.hashCode()).isEqualTo(expectedDto.hashCode());
    }

    @DisplayName("Результат Метода update не должен содержать Lazy полей")
    @Test
    void resultShouldNoHaveLazyFieldsUpdate() {
        String newText = "WOW";
        Commentary oldCommentary = em.find(Commentary.class,2);
        em.detach(oldCommentary);

        commentaryService.update(oldCommentary.getId(), oldCommentary.getBookId(), newText);

        Commentary updatedCommentary = em.find(Commentary.class,2);
        assertThat(updatedCommentary.getText()).isNotEqualTo(oldCommentary.getText()).isEqualTo(newText);
    }

    private static List<CommentaryDto> getDbCommentaries() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new CommentaryDto(id, (id - 1) / 2 + 1, "Comment_" + id))
                .toList();
    }
}