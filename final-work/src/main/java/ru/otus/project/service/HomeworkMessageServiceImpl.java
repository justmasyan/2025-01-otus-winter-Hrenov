package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.converter.HomeworkMessageConverter;
import ru.otus.project.dto.HomeworkMessageDto;
import ru.otus.project.repository.HomeworkMessageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeworkMessageServiceImpl {

    private final HomeworkMessageRepository messageRepository;

    private final HomeworkMessageConverter messageConverter;

    @Transactional(readOnly = true)
    public List<HomeworkMessageDto> findAllByHomeworkId(long homeworkId) {
        return messageRepository.findAllByHomeworkId(homeworkId).stream()
                .map(messageConverter::entityToWithAccountDto).toList();
    }
}
