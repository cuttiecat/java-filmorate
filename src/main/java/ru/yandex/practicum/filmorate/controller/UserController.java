package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;


import java.util.List;


@Slf4j
@RestController
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        log.info("Получение списка пользователей");
        return userService.findAll();
    }

    @PostMapping("/users")
    public User add(@RequestBody User user) {
        log.info("Добавили нового пользователя");
        return userService.add(user);
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {
        log.info("Пользователь с id: " + user.getId() + " обновлён");
        return userService.update(user);
    }

    @GetMapping("/users/{id}")
    public User findById(@PathVariable long id) {
        log.info("Поиск пользователя по ID: " + id);
        return userService.findById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable long id, @PathVariable long friendId) {
        log.info("Добавили пользователя по id: " + friendId + " в список друзей с id: " + id);
        userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeUserFromFriends(@PathVariable long id, @PathVariable long friendId) {
        log.info("Удалили пользователя по id: " + friendId + " из списка друзей с id: " + id);
        userService.deleteFromFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getUserFriends(@PathVariable long id) {
        log.info("Получение всех друзей по id пользователя: " + id);
        return userService.findAllFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("Получение всех общих друзей по id " + id + " и " + otherId);
        return userService.commonFriends(id, otherId);
    }
}