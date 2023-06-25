package ru.yandex.practicum.filmorate.storage;

public interface LikeStorage {
    void addLikeToFilm(long filmId, long userId);

    void removeLikeFromFilm(long filmId, long userId);
}
