package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            String studentAnswer = ioService.readStringWithPrompt(question.text());

            boolean isAnswerValid;
            if (question.answers() != null) {
                Optional<Answer> correctAnswer = question.answers()
                        .stream()
                        .filter(answer -> answer.text().equals(studentAnswer) && answer.isCorrect())
                        .findFirst();
                isAnswerValid = correctAnswer.isPresent();
            } else {
                isAnswerValid = true;
            }

            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
