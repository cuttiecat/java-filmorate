package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Service
@Slf4j
public class ValidationService {
    private static final LocalDate LIMIT_DATE = LocalDate.of(1895, 12, 28);
    private static final String USER_VALIDATION_MESSAGE = "User validation failed";
    private static final String FILM_VALIDATION_MESSAGE = "Film validation failed";

    public void validate(User user) {
        if (user.getEmail() == null ||
                user.getEmail().isBlank() ||
                !user.getEmail().contains("@") ||
                user.getLogin().isBlank() ||
                user.getLogin().contains(" ") ||
                user.getBirthday().isAfter(LocalDate.now())) {
            log.warn(USER_VALIDATION_MESSAGE);
            throw new ValidationException(USER_VALIDATION_MESSAGE);
        }
    }

    public void validate(Film film) {
        if (film.getName() == null ||
                film.getName().isBlank() ||
                film.getDescription().length() > 200 ||
                film.getReleaseDate().isBefore(LIMIT_DATE) ||
                film.getDuration() <= 0) {
            log.warn(FILM_VALIDATION_MESSAGE);
            throw new ValidationException(FILM_VALIDATION_MESSAGE);
        }
    }
}