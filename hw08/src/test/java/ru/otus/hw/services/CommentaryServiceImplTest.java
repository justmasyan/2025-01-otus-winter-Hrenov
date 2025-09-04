package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentaryConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentaryDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.TestDataProvider.getDbBooks;
import static ru.otus.hw.TestDataProvider.getDbComments;

@DataMongoTest()
@Import({AuthorServiceImpl.class,BookServiceImpl.class,
        GenreServiceImpl.class, CommentaryServiceImpl.class, CommentaryConverter.class,
        BookConverter.class, AuthorConverter.class, GenreConverter.class})
class CommentaryServiceImplTest {

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private CommentaryService commentService;

    @ParameterizedTest
    @MethodSource("ru.otus.hw.TestDataProvider#getDbComments")
    void findById(CommentaryDto expectedComment) {
        assertThat(commentService.findById(expectedComment.getId()))
                .isPresent().get().isEqualTo(expectedComment);

    }

    @ParameterizedTest
    @MethodSource("ru.otus.hw.TestDataProvider#getDbBooks")
    void findAllByBookId(BookDto bookDto) {
        List<CommentaryDto> expectedComments = getDbComments().stream()
                .filter(commentaryDto -> commentaryDto.getBook().getId().equals(bookDto.getId()))
                .peek(commentaryDto -> commentaryDto.setBook(null))
                .toList();
        assertThat(commentService.findAllByBookId(bookDto.getId()))
                .isEqualTo(expectedComments);
    }

    @Test
    @DirtiesContext
    void insert() {
        CommentaryDto expectedDto = new CommentaryDto("0", getDbBooks().get(0), "NEW_COMMENT");

        CommentaryDto actualDto = commentService.insert(expectedDto.getBook().getId(), expectedDto.getText());

        expectedDto.setId(actualDto.getId());
        assertThat(actualDto).isEqualTo(expectedDto);
    }

    @Test
    @DirtiesContext
    void update() {
        CommentaryDto expectedDto = new CommentaryDto("3", getDbBooks().get(2), "NEW_COMMENT");

        CommentaryDto actualDto = commentService.update(expectedDto.getId(),
                expectedDto.getBook().getId(),
                expectedDto.getText()
        );

        assertThat(actualDto).isEqualTo(expectedDto);
    }

    @Test
    @DirtiesContext
    void deleteById() {
        CommentaryDto commentDto = getDbComments().get(0);
        commentService.deleteById(commentDto.getId());

        Query searchQuery = new Query(Criteria.where("id").is(commentDto.getId()));
        assertThat(mongoOperations.findOne(searchQuery, CommentaryDto.class)).isNull();
    }
}