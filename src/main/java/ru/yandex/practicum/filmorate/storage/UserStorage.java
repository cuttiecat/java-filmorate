package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User add(User user);

    User findById(long id);

    List<User> findAll();

    User update(User user);

    void deleteUser(long id);
}
