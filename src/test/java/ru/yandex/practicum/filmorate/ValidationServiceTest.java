package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidationService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationServiceTest {

    @Test
    public void validateUserWithLoginBlank() {
        User user = new User();
        user.setName("Name");
        user.setLogin("");
        user.setEmail("email@email.ru");
        user.setBirthday(LocalDate.of(1980, 11, 15));

        assertThrows(ValidationException.class, () -> ValidationService.validate(user));
    }

    @Test
    public void validateUserWithLoginWrong() {
        User user = new User();
        user.setName("Name");
        user.setLogin("Log in");
        user.setEmail("email@email.ru");
        user.setBirthday(LocalDate.of(1980, 11, 15));

        assertThrows(ValidationException.class, () -> ValidationService.validate(user));
    }

    @Test
    public void validateUserWithNullEmail() {
        User user = new User();
        user.setName("Name");
        user.setLogin("Login");
        user.setBirthday(LocalDate.of(1980, 11, 15));

        assertThrows(ValidationException.class, () -> ValidationService.validate(user));
    }

    @Test
    public void validateUserWithWrongEmail() {
        User user = new User();
        user.setName("Name");
        user.setLogin("Login");
        user.setEmail("emailemail.ru");
        user.setBirthday(LocalDate.of(1980, 11, 15));

        assertThrows(ValidationException.class, () -> ValidationService.validate(user));
    }

    @Test
    public void validateUserWithBlankEmail() {
        User user = new User();
        user.setName("Name");
        user.setLogin("Login");
        user.setEmail("");
        user.setBirthday(LocalDate.of(1980, 11, 15));

        assertThrows(ValidationException.class, () -> ValidationService.validate(user));
    }

    @Test
    public void cWithWrongBirthday() {
        User user = new User();
        user.setName("Name");
        user.setLogin("Login");
        user.setEmail("email@email.ru");
        user.setBirthday(LocalDate.of(3000, 11, 15));

        assertThrows(ValidationException.class, () -> ValidationService.validate(user));
    }

    @Test
    public void validateFilmWithoutName() {
        Film film = new Film();
        film.setDuration(60);
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1980, 12, 30));

        assertThrows(ValidationException.class, () -> ValidationService.validate(film));
    }

    @Test
    public void validateFilmWithBlankName() {
        Film film = new Film();
        film.setName("");
        film.setDuration(60);
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1980, 12, 30));

        assertThrows(ValidationException.class, () -> ValidationService.validate(film));
    }

    @Test
    public void validateFilmWithWrongDescription() {
        Film film = new Film();
        film.setName("Name");
        film.setDuration(60);
        film.setDescription("Descriptionvnjvsdvnsdnvjsdnvjksdnvjksdnvjksndkjvnsdjkvnsdkjvnsdkjnvksjdnvkjsdnvkssdnvjksndvkjsndvjknsdjvnsjdkvnjksdnvjksdnvjsndjkvnsdkjvnsjkdnvjksdnvjksdnvjknsdjkvnsdkjvnskdjvnksjdnvkjsdnvjksdnvkjsndkvnsdkj");
        film.setReleaseDate(LocalDate.of(1980, 12, 30));

        assertThrows(ValidationException.class, () -> ValidationService.validate(film));
    }

    @Test
    public void validateFilmWithWrongReleaseDate() {
        Film film = new Film();
        film.setName("");
        film.setDuration(60);
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1800, 12, 30));

        assertThrows(ValidationException.class, () -> ValidationService.validate(film));
    }

    @Test
    public void validateFilmWithWrongDuration() {
        Film film = new Film();
        film.setName("");
        film.setDuration(-60);
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1980, 12, 30));

        assertThrows(ValidationException.class, () -> ValidationService.validate(film));
    }
}