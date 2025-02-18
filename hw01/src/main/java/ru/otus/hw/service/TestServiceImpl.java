package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> questions = questionDao.findAll();
        questions.forEach(question -> {
            ioService.printLine(question.text());
            if (question.answers() != null) {
                StringBuilder template = new StringBuilder();
                for (int i = 0; i < question.answers().size(); i++) {
                    template.append(i + 1).append(") %s; ");
                }
                Object[] variantsAnswers = question.answers().stream().map(Answer::text).toArray();
                ioService.printFormattedLine(template.toString(), variantsAnswers);
            }
            ioService.printLine("");
        });
    }
}
