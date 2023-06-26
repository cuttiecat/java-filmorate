package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User add(User user) {
        ValidationService.validate(user);
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        for (User userStr : userStorage.findAll()) {
            if (userStr.getName().equals(user.getName()) ||
                    userStr.getEmail().equals(user.getEmail()) ||
                    userStr.getLogin().equals(user.getLogin()) ||
                    userStr.getBirthday() == user.getBirthday()) {
                throw new UserAlreadyExistException("Такой пользователь уже существует!");
            }
        }
        return userStorage.add(user);
    }

    public User update(User user) {
        ValidationService.validate(user);
        if (userStorage.findById(user.getId()) == null) {
            throw new RuntimeException("User for update not found");
        }
        userStorage.update(user);
        return user;
    }

    public User findById(long id) {
        User user = userStorage.findById(id);
        if (user == null) {
            throw new RuntimeException("User by id: " + id + " not found");
        }
        return user;
    }

    public void addToFriends(long userId, long anotherUser) {
        User user1 = userStorage.findById(userId);
        User user2 = userStorage.findById(anotherUser);

        if (user1 == null || user2 == null) {
            throw new RuntimeException("Невозможно добавить пользователя в друзья т.к. один из пользователей не найден");
        }

        friendStorage.addToFriends(userId, anotherUser);
    }

    public void deleteFromFriends(long userId, long anotherUserId) {
        User user1 = userStorage.findById(userId);
        User user2 = userStorage.findById(anotherUserId);

        if (user1 == null || user2 == null) {
            throw new RuntimeException("Невозможно удалить пользователя из друзей т.к. один из пользователей не найден");
        }

        friendStorage.removeFromFriends(userId, anotherUserId);
    }

    public List<User> findAllFriends(long userId) {
        User user = userStorage.findById(userId);

        if (user == null)
            throw new RuntimeException("Пользователь не найден");

        return friendStorage.findAllFriends(userId);
    }

    public List<User> commonFriends(long userId, long anotherUserId) {
        List<User> userFriends = findAllFriends(userId);
        List<User> anotherFriends = findAllFriends(anotherUserId);
        List<User> commonFriends = new ArrayList<>();

        for (User user1friend : userFriends) {
            if (anotherFriends.contains(user1friend))
                commonFriends.add(user1friend);
        }

        return commonFriends;
    }
}
