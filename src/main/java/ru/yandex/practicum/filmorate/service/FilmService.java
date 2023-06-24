package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService {
    @Autowired
    FilmStorage filmStorage;
    @Autowired
    UserStorage userStorage;
    long id = 1;

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(long id) {
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new RuntimeException("Фильм не найден");
        }
        return film;
    }

    public Film add(Film film) {
        ValidationService.validate(film);
        film.setId(id++);
        filmStorage.add(film);
        return film;
    }

    public Film update(Film film) {
        ValidationService.validate(film);
        if (filmStorage.getById(film.getId()) == null) {
            throw new RuntimeException("Фильм для обновления не найден");
        }
        filmStorage.add(film);
        return film;
    }

    public void likeFilm(long id, long userId) {
        if (userStorage.getById(userId) == null) {
            throw new RuntimeException("Пользователь не найден");
        }
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new RuntimeException("Фильм не найден");
        }
        film.addToUsersWhoLikes(userId);
    }

    public void removeLike(long id, long userId) {
        if (userStorage.getById(userId) == null) {
            throw new RuntimeException("Пользователь не найден");
        }
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new RuntimeException("Фильм не найден");
        }
        film.removeFromUsersWhoLikes(userId);
    }

    public List<Film> getTopFilms(int count) {
        List<Film> topFilms = new ArrayList<>();
        List<Film> allFilms = filmStorage.findAll();

        allFilms.sort((Film film1, Film film2) -> film2.getUsersWhoLikes().size() - film1.getUsersWhoLikes().size());

        if (allFilms.size() > count) {
            for (int i = 0; i < count; i++) {
                topFilms.add(allFilms.get(i));
            }
        } else {
            topFilms.addAll(allFilms);
        }
        return topFilms;
    }
}

