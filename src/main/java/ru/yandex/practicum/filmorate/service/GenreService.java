package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
public class GenreService {
    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre getGenreById(int id) {
        Genre genre = genreStorage.getGenreById(id);
        if (genre == null) {
            throw new RuntimeException("Жанр не найден");
        }
        return genre;
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }
}
