package com.aryunin.learningspring.dao;

import com.aryunin.learningspring.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> getAllPeople() {
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO person(name, birth_year) VALUES(?,?)", person.getName(), person.getBirthYear());
    }

    public Optional<Person> get(String name) {
        return jdbcTemplate.query("SELECT * FROM person WHERE name=?", new BeanPropertyRowMapper<>(Person.class), name).stream().findAny();
    }
}
