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
        return jdbcTemplate.query("SELECT person_id AS id, name, birth_year FROM person", new BeanPropertyRowMapper<>(Person.class));
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO person(name, birth_year) VALUES(?,?)", person.getName(), person.getBirthYear());
    }

    public Optional<Person> get(String name) {
        return jdbcTemplate.query("SELECT person_id AS id, name, birth_year FROM person WHERE name=?", new BeanPropertyRowMapper<>(Person.class), name).stream().findAny();
    }

    public Optional<Person> get(int id) {
        return jdbcTemplate.query("SELECT person_id AS id, name, birth_year FROM person WHERE person_id=?", new BeanPropertyRowMapper<>(Person.class), id).stream().findAny();
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE person_id=?", id);
    }

    public void update(int id, Person person) {
        jdbcTemplate.update("UPDATE person SET name=?, birth_year=? WHERE person_id=?", person.getName(), person.getBirthYear(), id);
    }
}
