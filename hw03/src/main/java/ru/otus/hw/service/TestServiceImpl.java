package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            String studentAnswer = ioService.readStringWithPrompt(getFullText(question));
            testResult.applyAnswer(question, isCorrectAnswer(question, studentAnswer));
        }
        return testResult;
    }

    private String getFullText(Question question) {
        StringBuilder questionWithAnswers = new StringBuilder("\n").append(question.text());
        if (question.answers() != null) {
            StringBuilder templateAnswer = new StringBuilder("\n");
            for (int i = 0; i < question.answers().size(); i++) {
                templateAnswer.append(i + 1).append(") %s; ");
            }
            Object[] variantsAnswers = question.answers().stream().map(Answer::text).toArray();
            questionWithAnswers.append(String.format(templateAnswer.toString(), variantsAnswers));
        }
        return questionWithAnswers.toString();
    }

    private Boolean isCorrectAnswer(Question question, String studentAnswer) {
        if (question.answers() == null) {
            return true;
        }

        Optional<Answer> correctAnswer = question.answers()
                .stream()
                .filter(answer -> answer.text().equals(studentAnswer) && answer.isCorrect())
                .findFirst();
        return correctAnswer.isPresent();
    }

}
