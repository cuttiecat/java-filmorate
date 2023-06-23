package ru.yandex.practicum.filmorate.FilmTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

public class FilmValidationTests {
    private Validator validator;
    private Film film;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        factory.getConstraintValidatorFactory();
        validator = factory.getValidator();
        film = new Film();
    }

    @Test
    public void incorrectFormatOfFilmIdShouldFailValidation() {
        film.setId(123);

        film.setName("Зеленая миля");
        film.setReleaseDate(LocalDate.of(2018, 1, 1));
        film.setDuration(155);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void correctFormatOfFilmIdShouldFailValidation() {
        film.setId(null);

        film.setName("Зеленая миля");
        film.setReleaseDate(LocalDate.of(2018, 1, 1));
        film.setDuration(155);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void incorrectFormatOfFilmNameShouldFailValidation1() {
        film.setName("   ");
        film.setReleaseDate(LocalDate.of(2018, 1, 1));
        film.setDuration(155);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void incorrectFormatOfFilmNameShouldFailValidation2() {
        film.setName("");
        film.setReleaseDate(LocalDate.of(2018, 1, 1));
        film.setDuration(155);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void incorrectFormatOfFilmDescCharLengIs201ShouldFailValidation() {
        film.setName("Зеленая миля");
        film.setReleaseDate(LocalDate.of(2018, 1, 1));
        film.setDuration(155);
        film.setDescription("Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod" +
                " tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud " +
                "exerci tatios");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void correctFormatOfFilmDescCharLengIs200ShouldPassValidation() {
        film.setName("Зеленая миля");
        film.setReleaseDate(LocalDate.of(2018, 1, 1));
        film.setDuration(155);
        film.setDescription("Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod" +
                " tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud " +
                "exerci tatio");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void incorrectFormatOfFilmReleaseDateShouldFailValidation() {
        film.setName("Зеленая миля");
        film.setDuration(155);
        film.setDescription("Lo");
        film.setReleaseDate(null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void correctFormatOfFilmReleaseDateShouldPassValidation() {
        film.setName("Зеленая миля");
        film.setDuration(155);
        film.setDescription("Lo");
        film.setReleaseDate(LocalDate.of(2018, 1, 1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void incorrectFormatOfFilmDurationShouldFailValidation() {
        film.setName("Зеленая миля");
        film.setDuration(0);
        film.setDescription("Lo");
        film.setReleaseDate(LocalDate.of(2018, 1, 1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void correctFormatOfFilmDurationShouldFailValidation() {
        film.setName("Зеленая миля");
        film.setDuration(155);
        film.setDescription("Lo");
        film.setReleaseDate(LocalDate.of(2018, 1, 1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertTrue(violations.isEmpty());
    }

}