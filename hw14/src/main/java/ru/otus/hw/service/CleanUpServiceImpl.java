package ru.otus.hw.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CleanUpServiceImpl implements CleanUpService {

    private final EntityManager entityManager;

    @Override
    @Transactional
    public void cleanUp() {
        entityManager.createQuery("DELETE FROM CommentaryJpa").executeUpdate();
        entityManager.createQuery("DELETE FROM BookJpa").executeUpdate();
        entityManager.createQuery("DELETE FROM GenreJpa").executeUpdate();
        entityManager.createQuery("DELETE FROM AuthorJpa").executeUpdate();
    }
}
