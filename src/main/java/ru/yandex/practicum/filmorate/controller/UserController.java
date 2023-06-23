package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private int userId = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@RequestBody @Valid User user) throws ValidationException {
        log.debug("Добавление пользователя {}", user);
        UserValidator.validate(user);
        if (users.values().stream().noneMatch(u -> u.getLogin().equals(user.getLogin()))) {
            user.setId(userId++);
            users.put(user.getId(), user);
            log.error("User c логином {} добавлен", user.getLogin());
            return user;
        } else {
            log.error("User с логином {} уже существует", user.getLogin());
            throw new RuntimeException("User с таким логином уже существует");
        }
    }

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        log.debug("Обновление данных пользователя {}", user);

        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            log.error("User с id = {} не найден", user.getId());
            throw new RuntimeException("User с таким id не найден");
        }
    }
}