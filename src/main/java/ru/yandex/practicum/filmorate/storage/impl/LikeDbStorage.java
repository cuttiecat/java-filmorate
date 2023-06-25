package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Component
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLikeToFilm(long filmId, long userId) {
        String sqlQuery = "insert into likes (user_id, film_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    @Override
    public void removeLikeFromFilm(long filmId, long userId) {
        String sqlQuery = "delete from likes where user_id = ? and film_id = ?";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }
}
