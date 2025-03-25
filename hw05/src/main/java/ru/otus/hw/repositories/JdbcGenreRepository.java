package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {

    private final JdbcOperations jdbcOperations;


    @Override
    public List<Genre> findAll() {
        return jdbcOperations.query("SELECT id, name FROM genres", new GenreRowMapper());
    }

    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        String findAllByIdsQuery = "SELECT id, name FROM genres WHERE id IN (?" +
                ", ?".repeat(ids.size() - 1) + ")";
        return jdbcOperations.query(findAllByIdsQuery, new GenreRowMapper(), ids.toArray());
    }

    private static class GenreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            return new Genre(rs.getLong("id"), rs.getString("name"));
        }
    }
}
