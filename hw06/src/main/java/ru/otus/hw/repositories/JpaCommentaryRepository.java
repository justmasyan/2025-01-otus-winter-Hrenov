package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Commentary;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JpaCommentaryRepository implements CommentaryRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Commentary> findById(long id) {
        return Optional.ofNullable(em.find(Commentary.class, id));
    }

    @Override
    public List<Commentary> findAllByBookId(long bookId) {
        TypedQuery<Commentary> query = em.createQuery(
                "SELECT c FROM Commentary c WHERE c.book.id = :bookId", Commentary.class);
        query.setParameter("bookId", bookId);
        return query.getResultList();
    }

    @Override
    public Commentary save(Commentary commentary) {
        if (commentary.getId() == 0) {
            em.persist(commentary);
            return commentary;
        }
        return em.merge(commentary);
    }

    @Override
    public void deleteById(long id) {
        findById(id).ifPresent(em::remove);
    }
}
