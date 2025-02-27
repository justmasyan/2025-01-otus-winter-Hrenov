package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TestServiceImplTest {

    private IOService ioService;

    private QuestionDao questionDao;

    private TestServiceImpl testService;

    @BeforeEach
    void setUp() {
        ioService = mock(IOService.class);
        questionDao = mock(QuestionDao.class);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    void executeTest() {
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

        List<String> exceptedOutputQuestions = List.of(
                "Please answer the questions below\r\n",
                "Is there life on Mars?",
                "1) Science doesn't know this yet; 2) Certainly. The red UFO is from Mars. And green is from Venus; 3) Absolutely not; ",
                "How should resources be loaded form jar in Java?",
                "1) ClassLoader#geResourceAsStream or ClassPathResource#getInputStream; 2) ClassLoader#geResource#getFile + FileReader; 3) Wingardium Leviosa; ",
                "Which option is a good way to handle the exception?",
                "1) @SneakyThrow; 2) e.printStackTrace(); 3) Rethrow with wrapping in business exception (for example, QuestionReadException); 4) Ignoring exception; ",
                "What is the meaning of life?",
                "What number did I make a wish for?",
                "1) 0; 2) 1; 3) 2; 4) 3; 5) 4; 6) 5; 7) 6; 8) 7; 9) 8; 10) 9; "
        );


        List<String> actualOutputStrings = new ArrayList<>();

        doAnswer(invocation -> {
            actualOutputStrings.add(invocation.getArgument(0));
            return null;
        }).when(ioService).printLine(anyString());

        doAnswer(invocation -> {
            Object[] allArguments = invocation.getArguments();
            Object[] argumentsWithoutFirst = Arrays.copyOfRange(allArguments, 1, allArguments.length);
            actualOutputStrings.add(String.format((String) allArguments[0], argumentsWithoutFirst));
            return null;
        }).when(ioService).printFormattedLine(anyString(), any(Object[].class));

        when(questionDao.findAll()).thenReturn(expectedQuestions);

        testService.executeTest();

        actualOutputStrings.retainAll(exceptedOutputQuestions);

        assertThat(actualOutputStrings).isEqualTo(exceptedOutputQuestions);
    }
}