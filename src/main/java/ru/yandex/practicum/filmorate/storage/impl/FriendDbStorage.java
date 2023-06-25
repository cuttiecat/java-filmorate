package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addToFriends(long userId, long friendId) {
        String sqlQuery = "insert into friendships (user_id, friend_id, status) values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                userId,
                friendId,
                "unconfirmed");
    }

    @Override
    public void removeFromFriends(long userId, long friendId) {
        String sqlQuery = "delete from friendships where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery,
                userId,
                friendId);
    }

    @Override
    public List<User> findAllFriends(long userId) {
        List<User> friends = new ArrayList<>();
        String sqlQuery = "select us.* " +
                "from friendships as fr " +
                "join users as us on fr.friend_id=us.user_id " +
                "where fr.user_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        while (rowSet.next()) {
            LocalDate birthday = dateFormatter(Objects.requireNonNull(rowSet.getString("birthday")));
            User friend = new User(rowSet.getLong("user_id"),
                    rowSet.getString("email"),
                    rowSet.getString("login"),
                    rowSet.getString("name"),
                    birthday);
            friends.add(friend);
        }
        return friends;
    }

    private LocalDate dateFormatter(String date) {
        String[] parts = date.split("-");
        return LocalDate.of(Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]));
    }
}
