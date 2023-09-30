package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component("FilmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film add(Film film) {
        String sqlQuery = "insert into films (name, description, release_date, duration, mpa_id) " +
                "values (?, ?, ?, ?, ?)";
        int mpaId = film.getMpa().getId();
        film.setGenres(getUniqueGenres(film.getGenres()));

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                mpaId);

        String sqlQueryForLastId = "select film_id from films order by film_id desc limit 1";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQueryForLastId);
        if (rowSet.next()) {
            film.setId(rowSet.getLong("film_id"));
            if (film.getGenres().size() > 0) {
                String genreSqlQuery = "insert into film_genre (film_id, genre_id) " +
                        "values (?, ?)";

                jdbcTemplate.batchUpdate(genreSqlQuery, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, String.valueOf(film.getId()));
                        ps.setString(2, String.valueOf(film.getGenres().get(i).getId()));
                    }

                    @Override
                    public int getBatchSize() {
                        return film.getGenres().size();
                    }
                });
            }
            return film;
        } else {
            return null;
        }
    }

    @Override
    public Film getById(long id) {
        Film film = null;
        String sqlQuery = "select * " +
                "from films as f " +
                "join mpa as m on f.mpa_id = m.mpa_id " +
                "left join film_genre as fg on f.film_id = fg.film_id " +
                "left join genre as g on fg.genre_id = g.genre_id " +
                "where f.film_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        while (rowSet.next()) {
            Genre genre = new Genre(rowSet.getInt("genre_id"),
                    rowSet.getString("genre_name"));
            if (film == null) {
                Mpa mpa = new Mpa(rowSet.getInt("mpa_id"), rowSet.getString("mpa_name"));
                film = new Film(rowSet.getInt("film_id"),
                        rowSet.getString("name"),
                        rowSet.getString("description"),
                        dateFormatter(Objects.requireNonNull(rowSet.getString("release_date"))),
                        rowSet.getInt("duration"),
                        mpa);
                if (genre.getId() != 0)
                    film.addGenre(genre);
            } else {
                film.addGenre(genre);
            }
        }
        return film;
    }

    @Override
    public List<Film> findAll() {
        Map<Long, ArrayList<Genre>> filmGenres = getGenresMap();

        List<Film> filmList = new ArrayList<>();
        String sqlQuery = "select * " +
                "from films as f " +
                "join mpa as m on f.mpa_id=m.mpa_id ";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery);
        while (rowSet.next()) {
            Mpa mpa = new Mpa(rowSet.getInt("mpa_id"),
                    rowSet.getString("mpa_name"));
            long filmId = rowSet.getLong("film_id");
            Film film = new Film(filmId,
                    rowSet.getString("name"),
                    rowSet.getString("description"),
                    dateFormatter(Objects.requireNonNull(rowSet.getString("release_date"))),
                    rowSet.getInt("duration"),
                    mpa);
            if (filmGenres.containsKey(filmId)) {
                film.setGenres(filmGenres.get(filmId));
            }
            filmList.add(film);
        }
        return filmList;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "update films set " +
                "name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "where film_id = ?";

        film.setGenres(getUniqueGenres(film.getGenres()));

        int mpaId = film.getMpa().getId();

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                mpaId,
                film.getId());

        String genreRemoveSqlQuery = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(genreRemoveSqlQuery, film.getId());

        if (film.getGenres().size() > 0) {
            String genreSqlQuery = "insert into film_genre (film_id, genre_id) " +
                    "values (?, ?)";

            jdbcTemplate.batchUpdate(genreSqlQuery, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, String.valueOf(film.getId()));
                    ps.setString(2, String.valueOf(film.getGenres().get(i).getId()));
                }

                @Override
                public int getBatchSize() {
                    return film.getGenres().size();
                }
            });
        }

        return film;
    }

    @Override
    public void deleteFilm(long id) {
        String sqlQuery = "delete from films where film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public List<Film> getTopFilms(int count) {
        Map<Long, ArrayList<Genre>> filmGenres = getGenresMap();

        String sqlQuery = "SELECT f.*, count(l.user_id) AS likes, mpa.mpa_name " +
                "FROM FILMS AS f " +
                "LEFT JOIN likes AS l ON f.film_id=l.film_id " +
                "LEFT JOIN mpa AS mpa ON f.mpa_id=mpa.mpa_id " +
                "GROUP BY f.film_id " +
                "ORDER BY likes DESC  " +
                "LIMIT ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, count);
        List<Film> filmRating = new ArrayList<>();
        while (rowSet.next()) {
            long filmId = rowSet.getLong("film_id");
            Film film = new Film(filmId,
                    rowSet.getString("name"),
                    rowSet.getString("description"),
                    dateFormatter(Objects.requireNonNull(rowSet.getString("release_date"))),
                    rowSet.getInt("duration"),
                    new Mpa(rowSet.getInt("mpa_id"), rowSet.getString("mpa_name"))
            );
            if (filmGenres.containsKey(filmId)) {
                film.setGenres(filmGenres.get(filmId));
            }
            filmRating.add(film);
        }
        return filmRating;
    }

    private LocalDate dateFormatter(String date) {
        String[] parts = new String[3];
        parts = date.split("-");
        LocalDate localDate = LocalDate.of(Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]));
        return localDate;
    }

    private List<Genre> getUniqueGenres(List<Genre> genres) {
        List<Genre> uniqueGenres = new ArrayList<>();
        for (Genre genre : genres) {
            if (!uniqueGenres.contains(genre)) {
                uniqueGenres.add(genre);
            }
        }
        return uniqueGenres;
    }

    private Map<Long, ArrayList<Genre>> getGenresMap() {
        Map<Long, ArrayList<Genre>> filmGenres = new HashMap<>();
        String genresSqlQuery = "select * " +
                "from film_genre as fg " +
                "join genre as g on fg.genre_id = g.genre_id ";
        SqlRowSet genresRowSet = jdbcTemplate.queryForRowSet(genresSqlQuery);
        while (genresRowSet.next()) {
            long filmId = genresRowSet.getLong("film_id");
            Genre genre = new Genre(genresRowSet.getInt("genre_id"),
                    genresRowSet.getString("genre_name"));
            if (!filmGenres.containsKey(filmId)) {
                ArrayList<Genre> newGenreArrayList = new ArrayList<>();
                newGenreArrayList.add(genre);
                filmGenres.put(filmId, newGenreArrayList);
            } else {
                ArrayList<Genre> genreList = filmGenres.get(filmId);
                genreList.add(genre);
                filmGenres.remove(filmId);
                filmGenres.put(filmId, genreList);
            }
        }
        return filmGenres;
    }
}
