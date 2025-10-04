package ru.otus.project.service;

import ru.otus.project.dto.HomeworkMessageDto;

import java.util.List;

public interface HomeworkMessageService {


    List<HomeworkMessageDto> findAllByHomeworkId(long homeworkId);

    HomeworkMessageDto insertMessage(HomeworkMessageDto messageDto, long accountId);
}
