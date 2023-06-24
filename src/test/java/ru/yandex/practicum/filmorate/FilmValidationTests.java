//package ru.yandex.practicum.filmorate;
//
//import org.junit.jupiter.api.Test;
//
//import ru.yandex.practicum.filmorate.exceptions.ValidationException;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.validators.FilmValidator;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//public class FilmValidationTests {
//    private Film film;
//
//    @Test
//    public void shouldSuccessfullyValidateCorrectFilm() {
//        film = new Film("Тайна Коко", "шедевр",
//                LocalDate.of(2017, 1, 1), 105);
//        assertDoesNotThrow(() -> FilmValidator.validate(film));
//    }
//
//    @Test
//    public void shouldThrowExceptionWhenNameIsEmpty() {
//        film = new Film("", "шедевр",
//                LocalDate.of(2017, 1, 1), 105);
//        assertThrows(ValidationException.class, () -> FilmValidator.validate(film));
//    }
//
//    @Test
//    public void shouldNotThrowExceptionWhenReleaseDateIs28_12_1895() {
//        film = new Film("Тайна Коко", "шедевр",
//                LocalDate.of(1895, 12, 28), 105);
//        assertDoesNotThrow(() -> FilmValidator.validate(film));
//    }
//
//    @Test
//    public void shouldThrowExceptionWhenReleaseDateIsBefore28_12_1895() {
//        film = new Film("Тайна Коко", "шедевр",
//                LocalDate.of(1895, 12, 27), 105);
//        assertThrows(ValidationException.class, () -> FilmValidator.validate(film));
//    }
//
//    @Test
//    public void shouldNotThrowExceptionWhenDurationIsPositive() {
//        film = new Film("Тайна Коко", "шедевр",
//                LocalDate.of(1895, 12, 28), 1);
//        assertDoesNotThrow(() -> FilmValidator.validate(film));
//    }
//
//    @Test
//    public void shouldThrowExceptionWhenDurationIsZero() {
//        film = new Film("Тайна Коко", "шедевр",
//                LocalDate.of(1895, 12, 27), 0);
//        assertThrows(ValidationException.class, () -> FilmValidator.validate(film));
//    }
//
//    @Test
//    public void shouldThrowExceptionWhenDurationIsNegative() {
//        film = new Film("Тайна Коко", "шедевр",
//                LocalDate.of(1895, 12, 28), -1);
//        assertThrows(ValidationException.class, () -> FilmValidator.validate(film));
//    }
//
//    @Test
//    public void shouldNotThrowExceptionWhenDescriptionIs200Symbols() {
//        String str = "1";
//        for (int i = 0; i < 199; i++) {
//            str += "w";
//        }
//
//        film = new Film("Тайна Коко", str,
//                LocalDate.of(1895, 12, 28), 144);
//        assertDoesNotThrow(() -> FilmValidator.validate(film));
//    }
//
//    @Test
//    public void shouldThrowExceptionWhenDescriptionIsLonger200Symbols() {
//        String str = "1";
//        for (int i = 0; i < 200; i++) {
//            str += "w";
//        }
//
//        film = new Film("Тайна Коко", str,
//                LocalDate.of(1895, 12, 28), 144);
//        assertThrows(ValidationException.class, () -> FilmValidator.validate(film));
//    }
//}