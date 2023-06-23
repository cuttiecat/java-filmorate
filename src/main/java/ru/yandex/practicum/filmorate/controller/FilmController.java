package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private int filmId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    public void validate(@RequestBody @Valid Film film) {
        FilmValidator.validate(film);
        if (films.values().stream().anyMatch(u -> u.getName().equals(film.getName()))) {
            log.error("Фильм с названием {} уже был добавлен", film.getName());
            throw new RuntimeException("Фильм с названием уже был добавлен в мапу");
        }
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        FilmValidator.validate(film);
        if (films.values().stream().noneMatch(u -> u.getName().equals(film.getName()))) {
            film.setId(filmId++);
            films.put(film.getId(), film);
            log.error("Фильм с названием {} добавлен!", film.getName());
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.debug("Обновление фильма {}", film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            log.error("Фильм с id = {} не найден", film.getId());
            throw new RuntimeException("Фильм с таким ID не найден");
        }
    }
}