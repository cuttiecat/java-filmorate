package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new LinkedHashMap<>();

    public void add(Film film) {
        films.put(film.getId(), film);
    }

    public Film getById(long id) {
        return films.get(id);
    }

    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    public List<Film> getTopFilms(int count) {
        List<Film> topFilms = new ArrayList<>();
        List<Film> allFilms = findAll();
        allFilms.sort((Film film1, Film film2) -> film2.getUsersWhoLikes().size() - film1.getUsersWhoLikes().size());

        if (allFilms.size() > count) {
            for (int i = 0; i < count; i++) {
                topFilms.add(allFilms.get(i));
            }
        } else {
            topFilms.addAll(allFilms);
        }
        return topFilms;
    }
}
