package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmValidator {
    private static final LocalDate BIRTHDAY_MOVIES = LocalDate.of(1895, 12, 28);

    public static void validate(Film film) {
        if (film.getDuration() <= 0) {
            log.info("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }
        if (film.getName() == null || film.getName().equals("")) {
            log.info("Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым!");
        }
        if (film.getReleaseDate().isBefore(BIRTHDAY_MOVIES)) {
            log.info("Дата релиза фильма должна быть после 28.12.1985");
            throw new ValidationException("Дата релиза фильма должна быть после 28.12.1985 !");
        }
        if (film.getDescription().length() > 200) {
            log.info("Описание фильма слишком длинное");
            throw new ValidationException("Описание фильма должно быть не больше 200 символов!");
        }
    }
}
