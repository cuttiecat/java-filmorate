package ru.yandex.practicum.filmorate.UserTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

public class UserValidationTests {
    private Validator validator;
    private User user;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        factory.getConstraintValidatorFactory();
        validator = factory.getValidator();
        user = new User();
    }

    @Test
    public void incorrectFormatOfEmailShouldFailValidation() {
        user.setEmail("java.com");
        user.setLogin("Grenka123");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void correctMailShouldPassValidation() {
        user.setEmail("java@gmail.com");
        user.setLogin("Grenka333");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void incorrectFormatOfIDShouldFailValidation() {
        user.setId(32);
        user.setEmail("java@gmail.com");
        user.setLogin("Grenka333");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void correctIDShouldPassValidation() {
        user.setId(null);
        user.setEmail("java@gmail.com");
        user.setLogin("Grenka333");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void incorrectFormatOfLoginShouldFailValidation1() {
        user.setEmail("java@gmail.com");
        user.setLogin("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void incorrectFormatOfLoginShouldFailValidation2() {
        user.setEmail("java@gmail.com");
        user.setLogin("       ");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void incorrectFormatOfBirthdayShouldFailValidation() {
        user.setEmail("java@gmail.com");
        user.setLogin("Bloller");
        user.setBirthday(LocalDate.of(2024, 10, 18));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void correctFormatOfBirthdayShouldFailValidation() {
        user.setEmail("java@gmail.com");
        user.setLogin("Bloller");
        user.setBirthday(LocalDate.of(1977, 10, 18));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertTrue(violations.isEmpty());
    }


}