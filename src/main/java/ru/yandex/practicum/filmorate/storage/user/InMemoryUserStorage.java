package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new LinkedHashMap<>();

    @Override
    public void add(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public User getById(long id) {
        return users.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public List<User> findAllFriends(long userId) {
        User user = getById(userId);
        List<User> users = new ArrayList<>();

        if (user == null)
            throw new RuntimeException("Пользователь не найден");

        for (Long friendId : user.getFriends()) {
            users.add(getById(friendId));
        }

        return users;
    }
}
