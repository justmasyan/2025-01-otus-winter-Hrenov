package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaAuthorRepository implements AuthorRepository {

    @PersistenceContext
    private final EntityManager em;

    @Autowired
    public JpaAuthorRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Author> findById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public List<Author> findAll() {
        TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a", Author.class);
        return query.getResultList();
    }
}
