package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidator {

    public static void validate(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().equals("")) {
            log.info("Email пустой");
            throw new ValidationException("Email пустой");
        }
        if (!user.getEmail().contains("@")) {
            log.info("Email должен содержать символ \"@\"");
            throw new ValidationException("Email должен содержать символ  \"@\".");
        }
        if (user.getLogin() == null || user.getLogin().equals("") || user.getLogin().contains(" ")) {
            log.info("Login неправильный");
            throw new ValidationException("Login неправильный");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("имя для отображения пустое — в таком случае будет использован логин");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Дата рождения указана неверно");
            throw new ValidationException("Дата рождения указана неверно");
        }
    }
}