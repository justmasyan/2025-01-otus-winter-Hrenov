package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CsvQuestionDao.class)
class CsvQuestionDaoTest {

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @MockitoBean
    private TestFileNameProvider fileNameProvider;

    @Test
    void simpleIntegrationTestFindAll() {
        List<Question> expectedQuestions = List.of(
                new Question("Is there life on Mars?", List.of(
                        new Answer("Science doesn't know this yet", true),
                        new Answer("Certainly. The red UFO is from Mars. And green is from Venus", false),
                        new Answer("Absolutely not", false))),
                new Question("How should resources be loaded form jar in Java?", List.of(
                        new Answer("ClassLoader#geResourceAsStream or ClassPathResource#getInputStream", true),
                        new Answer("ClassLoader#geResource#getFile + FileReader", false),
                        new Answer("Wingardium Leviosa", false))),
                new Question("Which option is a good way to handle the exception?",
                        List.of(new Answer("@SneakyThrow", false),
                                new Answer("e.printStackTrace()", false),
                                new Answer("Rethrow with wrapping in business exception (for example, QuestionReadException)", true),
                                new Answer("Ignoring exception", false))),
                new Question("What is the meaning of life?", null),
                new Question("What number did I make a wish for?", List.of(
                        new Answer("0", false),
                        new Answer("1", false),
                        new Answer("2", false),
                        new Answer("3", false),
                        new Answer("4", false),
                        new Answer("5", false),
                        new Answer("6", false),
                        new Answer("7", false),
                        new Answer("8", false),
                        new Answer("9", false)
                ))
        );

        when(fileNameProvider.getTestFileName()).thenReturn("questions.csv");

        assertThat(csvQuestionDao.findAll()).isEqualTo(expectedQuestions);
    }
}