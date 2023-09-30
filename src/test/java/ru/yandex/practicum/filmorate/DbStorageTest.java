package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DbStorageTest {
    private EmbeddedDatabase embeddedDatabase;
    private JdbcTemplate jdbcTemplate;
    private UserDbStorage userDbStorage;
    private FilmDbStorage filmDbStorage;
    private FriendDbStorage friendDbStorage;
    private GenreDbStorage genreDbStorage;
    private LikeDbStorage likeDbStorage;
    private MpaDbStorage mpaDbStorage;
    User user = new User(0,
            "mail@mail.ru",
            "userLogin",
            "userName",
            LocalDate.of(1991, 12, 15)
    );
    User updatedUser = new User(1,
            "updated@mail.ru",
            "updatedUserLogin",
            "updatedUserName",
            LocalDate.of(1995, 10, 17)
    );
    Film film = new Film(0,
            "filmName",
            "FilmDescription",
            LocalDate.of(1976, 5, 13),
            120,
            new Mpa(1)
    );
    Film updatedFilm = new Film(1,
            "UpdatedName",
            "UpdatedDescription",
            LocalDate.of(1977, 1, 5),
            125,
            new Mpa(2)
    );

    @BeforeEach
    void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addScript("schema.sql")
                .addScript("data.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
        jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        userDbStorage = new UserDbStorage(jdbcTemplate);
        filmDbStorage = new FilmDbStorage(jdbcTemplate);
        friendDbStorage = new FriendDbStorage(jdbcTemplate);
        genreDbStorage = new GenreDbStorage(jdbcTemplate);
        likeDbStorage = new LikeDbStorage(jdbcTemplate);
        mpaDbStorage = new MpaDbStorage(jdbcTemplate);
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Test
    void testAddUserToDb() {
        User returnedUser = userDbStorage.add(user);
        user.setId(1);
        assertEquals(user, returnedUser);
    }

    @Test
    void testFindUserByIdFromDb() {
        userDbStorage.add(user);
        User returnedUser = userDbStorage.findById(1);
        user.setId(1);
        assertEquals(user, returnedUser);
    }

    @Test
    void testUpdateUser() {
        userDbStorage.add(user);
        User returnedUser = userDbStorage.update(updatedUser);
        assertEquals(updatedUser, returnedUser);
    }

    @Test
    void testFindAllUsers() {
        userDbStorage.add(user);
        userDbStorage.add(updatedUser);
        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(updatedUser);
        List<User> returnedUserList = userDbStorage.findAll();

        assertEquals(2, returnedUserList.size());
        assertArrayEquals(userList.toArray(), returnedUserList.toArray());
    }

    @Test
    void testDeleteUser() {
        userDbStorage.add(user);
        userDbStorage.deleteUser(1);
        List<User> returnedUserList = userDbStorage.findAll();
        assertEquals(0, returnedUserList.size());
    }

    @Test
    void testFilmAdd() {
        Film returnedFilm = filmDbStorage.add(film);
        film.setId(1);
        assertEquals(film, returnedFilm);
    }

    @Test
    void testGetFilmById() {
        filmDbStorage.add(film);
        Film returnedFilm = filmDbStorage.getById(1);
        assertEquals(film.getName(), returnedFilm.getName());
        assertEquals(film.getDescription(), returnedFilm.getDescription());
        assertEquals(film.getMpa().getId(), returnedFilm.getMpa().getId());
        assertEquals(film.getDuration(), returnedFilm.getDuration());
        assertEquals(film.getReleaseDate(), returnedFilm.getReleaseDate());
    }

    @Test
    void testGetAllFilms() {
        filmDbStorage.add(film);
        filmDbStorage.add(updatedFilm);
        List<Film> returnedFilmList = filmDbStorage.findAll();
        assertEquals(2, returnedFilmList.size());
        assertEquals(returnedFilmList.get(0).getName(), film.getName());
        assertEquals(returnedFilmList.get(1).getName(), updatedFilm.getName());
    }

    @Test
    void testUpdateFilm() {
        filmDbStorage.add(film);
        filmDbStorage.update(updatedFilm);
        Film returnedFilm = filmDbStorage.getById(1);
        assertEquals(updatedFilm.getName(), returnedFilm.getName());
        assertEquals(updatedFilm.getDescription(), returnedFilm.getDescription());
        assertEquals(updatedFilm.getMpa().getId(), returnedFilm.getMpa().getId());
        assertEquals(updatedFilm.getDuration(), returnedFilm.getDuration());
        assertEquals(updatedFilm.getReleaseDate(), returnedFilm.getReleaseDate());
    }

    @Test
    void testDeleteFilm() {
        filmDbStorage.add(film);
        filmDbStorage.add(updatedFilm);
        filmDbStorage.deleteFilm(1);
        Film returnedFilm = filmDbStorage.getById(1);
        assertNull(returnedFilm);
    }

    @Test
    void testAddToFriendAndFindFriendsByUserId() {
        userDbStorage.add(user);
        userDbStorage.add(updatedUser);
        friendDbStorage.addToFriends(1, 2);
        List<User> returnedFriendList = friendDbStorage.findAllFriends(1);
        assertEquals(updatedUser, returnedFriendList.get(0));
        assertEquals(1, returnedFriendList.size());
    }

    @Test
    void testRemoveFromFriends() {
        userDbStorage.add(user);
        userDbStorage.add(updatedUser);
        friendDbStorage.addToFriends(1, 2);
        friendDbStorage.removeFromFriends(1, 2);
        List<User> returnedFriendList = friendDbStorage.findAllFriends(1);
        assertEquals(0, returnedFriendList.size());
    }

    @Test
    void testGetGenreById() {
        Genre returnedGenre = genreDbStorage.getGenreById(1);
        assertEquals("Комедия", returnedGenre.getName());
    }

    @Test
    void testGetAllGenres() {
        List<Genre> returnedGenresList = genreDbStorage.getAllGenres();
        assertEquals("Комедия", returnedGenresList.get(0).getName());
        assertEquals("Драма", returnedGenresList.get(1).getName());
        assertEquals("Мультфильм", returnedGenresList.get(2).getName());
        assertEquals("Триллер", returnedGenresList.get(3).getName());
        assertEquals("Документальный", returnedGenresList.get(4).getName());
        assertEquals("Боевик", returnedGenresList.get(5).getName());
    }

    @Test
    void testAddLikeAndGetTopFilms() {
        filmDbStorage.add(film);
        filmDbStorage.add(updatedFilm);
        userDbStorage.add(user);
        likeDbStorage.addLikeToFilm(2, 1);
        List<Film> returnedTopFilms = filmDbStorage.getTopFilms(1);
        assertEquals(updatedFilm.getName(), returnedTopFilms.get(0).getName());
        assertEquals(updatedFilm.getDuration(), returnedTopFilms.get(0).getDuration());
        assertEquals(updatedFilm.getDescription(), returnedTopFilms.get(0).getDescription());
    }

    @Test
    void testGetMpaById() {
        Mpa mpa = mpaDbStorage.getMpaById(1);
        assertEquals("G", mpa.getName());
    }

    @Test
    void testGetAllMpas() {
        List<Mpa> returnedMpaList = mpaDbStorage.getAllMpa();
        assertEquals("G", returnedMpaList.get(0).getName());
        assertEquals("PG", returnedMpaList.get(1).getName());
        assertEquals("PG-13", returnedMpaList.get(2).getName());
        assertEquals("R", returnedMpaList.get(3).getName());
        assertEquals("NC-17", returnedMpaList.get(4).getName());
    }
}
