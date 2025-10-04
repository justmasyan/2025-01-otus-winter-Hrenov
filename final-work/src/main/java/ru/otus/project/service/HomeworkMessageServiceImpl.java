package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.converter.HomeworkMessageConverter;
import ru.otus.project.domain.Account;
import ru.otus.project.domain.Homework;
import ru.otus.project.domain.HomeworkMessage;
import ru.otus.project.domain.Lector;
import ru.otus.project.domain.Student;
import ru.otus.project.dto.HomeworkMessageDto;
import ru.otus.project.exception.AccountNotFoundException;
import ru.otus.project.exception.AccountNotInHomeworkException;
import ru.otus.project.exception.EmptyHomeworkException;
import ru.otus.project.exception.HomeworkNotFoundException;
import ru.otus.project.exception.NoZeroIdException;
import ru.otus.project.repository.AccountRepository;
import ru.otus.project.repository.HomeworkMessageRepository;
import ru.otus.project.repository.HomeworkRepository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class HomeworkMessageServiceImpl implements HomeworkMessageService {

    private final AccountRepository accountRepository;

    private final HomeworkRepository homeworkRepository;

    private final HomeworkMessageRepository messageRepository;

    private final HomeworkMessageConverter messageConverter;

    @Override
    @Transactional(readOnly = true)
    public List<HomeworkMessageDto> findAllByHomeworkId(long homeworkId) {
        return messageRepository.findAllByHomeworkIdOrderBySendTimeDesc(homeworkId).stream()
                .map(messageConverter::entityToWithAccountDto).toList();
    }

    @Override
    @Transactional
    public HomeworkMessageDto insertMessage(HomeworkMessageDto messageDto, long accountId) {
        if (Objects.isNull(messageDto.getHomework()) || messageDto.getHomework().getId() == 0) {
            throw new EmptyHomeworkException("Дз не должно быть пустым");
        }

        if (messageDto.getId() != 0) {
            throw new NoZeroIdException("Id должно равняться 0");
        }

        Homework homework = homeworkRepository.findById(messageDto.getHomework().getId())
                .orElseThrow(() -> new HomeworkNotFoundException("Домашняя работа не найдена"));

        if (homework.getLector().getAccount().getId() != accountId && homework.getStudent().getAccount().getId() != accountId) {
            throw new AccountNotInHomeworkException("Данное ДЗ никак к вам не относится");
        }

        Account account = accountRepository.findById(accountId).
                orElseThrow(() -> new AccountNotFoundException("Данный аккаунт не обнаружен"));
        String senderDesc = getSenderDescByHomework(homework, accountId);

        HomeworkMessage message = new HomeworkMessage(
                messageDto.getId(),
                messageDto.getText(),
                new Date(),
                senderDesc,
                account,
                homework);
        HomeworkMessage savedMessage = messageRepository.save(message);
        return messageConverter.entityToWithAccountDto(savedMessage);
    }

    private String getSenderDescByHomework(Homework homework, long accountId) {
        if (homework.getStudent().getAccount().getId() == accountId) {
            Student student = homework.getStudent();
            return "%s %s".formatted(student.getFirstName(), student.getLastName());
        } else if (homework.getLector().getAccount().getId() == accountId) {
            Lector lector = homework.getLector();
            return "%s %s".formatted(lector.getFirstName(), lector.getLastName());
        } else {
            throw new AccountNotInHomeworkException("Данное ДЗ никак к вам не относится");
        }
    }
}
