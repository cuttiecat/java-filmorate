package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private InMemoryUserStorage inMemoryUserStorage;
    @Autowired
    private UserStorage userStorage;
    @Autowired
    ValidationService validationService;
    long id = 1;

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User add(User user) {
        validationService.validate(user);
        user.setId(id++);
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        userStorage.add(user);
        return user;
    }

    public User update(User user) {
        validationService.validate(user);
        if (userStorage.getById(user.getId()) == null) {
            throw new RuntimeException("Пользователь для обновления не найден");
        }
        userStorage.add(user);
        return user;
    }

    public User findById(long id) {
        User user = userStorage.getById(id);
        if (user == null) {
            throw new RuntimeException("Человек по id: " + id + " не найден");
        }
        return user;
    }

    public void addToFriends(long userId1, long userId2) {
        User user1 = userStorage.getById(userId1);
        User user2 = userStorage.getById(userId2);

        if (user1 == null || user2 == null) {
            throw new RuntimeException("Невозможно добавить пользователя в друзья т.к один из пользователей не найден");
        }

        user1.addFriend(userId2);
        user2.addFriend(userId1);
    }

    public void deleteFromFriends(long userId1, long userId2) {
        User user1 = userStorage.getById(userId1);
        User user2 = userStorage.getById(userId2);

        if (user1 == null || user2 == null) {
            throw new RuntimeException("Невозможно удалить пользователя из друзей т.к один из пользователей не найден");
        }

        user1.removeFriend(userId2);
        user2.removeFriend(userId1);
    }

    public List<User> findAllFriends(long userId) {
        return inMemoryUserStorage.findAllFriends(userId);
    }

    public List<User> commonFriends(long userId1, long userId2) {
        List<User> user1friends = findAllFriends(userId1);
        List<User> user2friends = findAllFriends(userId2);
        List<User> commonFriends = new ArrayList<>();

        for (User user1friend : user1friends) {
            if (user2friends.contains(user1friend))
                commonFriends.add(user1friend);
        }

        return commonFriends;
    }
}

