package ru.yandex.practicum.filmorate.UserTests;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidationTests {
    private User user;

    @Test
    public void shouldSuccessfullyValidateCorrectUser() {
        user = new User("java@gmail.com", "Крол33", "Катя",
                LocalDate.of(2001, 1, 29));
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsEmpty() {
        user = new User("", "Крол33", "Катя",
                LocalDate.of(2001, 1, 29));
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsNull() {
        user = new User(null, "Крол33", "Катя",
                LocalDate.of(2001, 1, 29));
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsWithoutAtSymbol() {
        user = new User("javagmail.com", "Крол33", "Катя",
                LocalDate.of(2001, 1, 29));
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    public void shouldThrowExceptionWhenLoginIsEmpty() {
        user = new User("java@gmail.com", "", "Катя",
                LocalDate.of(2001, 1, 29));
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    public void shouldThrowExceptionWhenLoginIsNull() {
        user = new User("java@gmail.com", null, "Катя",
                LocalDate.of(2001, 1, 29));
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    public void shouldThrowExceptionWhenLoginContainsSpace() {
        user = new User("java@gmail.com", "Крол 33", "Катя",
                LocalDate.of(2001, 1, 29));
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }

    @Test
    public void shouldThrowExceptionWhenBirthdayIsInFuture() {
        user = new User("java@gmail.com", "Крол33", "Катя",
                LocalDate.of(2030, 1, 29));
        assertThrows(ValidationException.class, () -> UserValidator.validate(user));
    }
    @Test
    public void shouldNotThrowExceptionWhenBirthdayIsToday() {
        user = new User("java@gmail.com", "Крол33", "Катя",
                LocalDate.now());
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }
}