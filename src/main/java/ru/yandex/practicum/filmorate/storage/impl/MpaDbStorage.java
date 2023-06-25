package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.ArrayList;
import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(int id) {
        String sqlQuery = "select * from mpa where mpa_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        Mpa mpa = null;
        if (rowSet.next()) {
            mpa = new Mpa(rowSet.getInt("mpa_id"), rowSet.getString("mpa_name"));
        }
        return mpa;
    }

    @Override
    public List<Mpa> getAllMpa() {
        List<Mpa> mpas = new ArrayList<>();
        String sqlQuery = "select * from mpa";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery);
        while (rowSet.next()) {
            Mpa mpa = new Mpa(rowSet.getInt("mpa_id"), rowSet.getString("mpa_name"));
            mpas.add(mpa);
        }
        return mpas;
    }
}
