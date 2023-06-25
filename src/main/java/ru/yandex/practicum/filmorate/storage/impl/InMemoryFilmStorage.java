package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    Map<Long, Film> films = new LinkedHashMap<>();

    public Film add(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    public Film getById(long id) {
        return films.get(id);
    }

    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public void deleteFilm(long id) {

    }

    @Override
    public List<Film> getTopFilms(int count) {
        return null;
    }
}
