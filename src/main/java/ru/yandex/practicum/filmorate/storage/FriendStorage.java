package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    void addToFriends(long userId, long friendId);

    void removeFromFriends(long userId, long friendId);

    List<User> findAllFriends(long userId);
}