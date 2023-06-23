package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private int filmId = 1;
    private static final LocalDate BIRTHDAY_MOVIES = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.debug("Добавление фильма {}", film);
        checkFilmDate(film);
        film.setId(filmId++);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.debug("Обновление фильма {}", film);
        if (!films.containsKey(film.getId())) {
            log.error("фильма c id {} нет", film.getId());
            throw new ValidationException("Фильм не найден для обновления!");
        }
        checkFilmDate(film);
        films.put(film.getId(), film);
        return film;
    }

    public void checkFilmDate(Film film) {
        if (film.getReleaseDate().isAfter(BIRTHDAY_MOVIES)) {
            log.error("дата релиза — не должна быть раньше 28 декабря 1895 года");
            throw new ValidationException("дата релиза — не должна быть раньше 28 декабря 1895 года;");
        }
    }
}