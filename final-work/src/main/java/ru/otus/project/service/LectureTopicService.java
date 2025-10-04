package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.converter.LectureTopicConverter;
import ru.otus.project.domain.LectureTopic;
import ru.otus.project.dto.LectureTopicDto;
import ru.otus.project.exception.NoZeroIdException;
import ru.otus.project.exception.TopicTitleExistException;
import ru.otus.project.repository.LectureTopicRepository;

import java.util.List;


public interface LectureTopicService {

    List<LectureTopicDto> findAll();

    LectureTopicDto insert(LectureTopicDto topicDto);
}
