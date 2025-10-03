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

@Service
@RequiredArgsConstructor
public class LectureTopicServiceImpl {

    private final LectureTopicRepository topicRepository;

    private final LectureTopicConverter topicConverter;

    public List<LectureTopicDto> findAll() {
        return topicRepository.findAll().stream()
                .map(topicConverter::entityToDto).toList();
    }

    @Transactional
    public LectureTopicDto insert(LectureTopicDto topicDto) {
        if (topicDto.getId() != 0) {
            throw new NoZeroIdException("Id должно равнять 0");
        }

        if (topicRepository.findByTitle(topicDto.getTitle()).isPresent()) {
            throw new TopicTitleExistException("Тема лекции с таким названием уже существует");
        }

        LectureTopic topic = topicRepository.save(new LectureTopic(topicDto.getId(), topicDto.getTitle()));
        return topicConverter.entityToDto(topic);
    }
}
