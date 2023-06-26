package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage,
                       LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(long id) {
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new RuntimeException("Film is not found");
        }
        return film;
    }

    public Film add(Film film) {
        ValidationService.validate(film);
        for (Film filmStr : filmStorage.findAll()) {
            if (filmStr.getName().equals(film.getName()) ||
                    filmStr.getDescription().equals(film.getDescription()) ||
                    filmStr.getReleaseDate().equals(film.getReleaseDate()) ||
                    filmStr.getDuration() == film.getDuration()) {
                throw new RuntimeException("Такой фильм уже существует!");
            }
        }
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        ValidationService.validate(film);
        if (filmStorage.getById(film.getId()) == null) {
            throw new RuntimeException("Фильм для обновления не найден");
        }
        return filmStorage.update(film);
    }

    public void likeFilm(long id, long userId) {
        if (!userStorage.existId(userId)) {
            throw new RuntimeException("User is not found");
        }
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new RuntimeException("Film is not found");
        }
        likeStorage.addLikeToFilm(id, userId);
    }

    public void removeLike(long id, long userId) {
        if (userStorage.findById(userId) == null) {
            throw new RuntimeException("User is not found");
        }
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new RuntimeException("Film is not found");
        }
        likeStorage.removeLikeFromFilm(id, userId);
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.getTopFilms(count);
    }
}