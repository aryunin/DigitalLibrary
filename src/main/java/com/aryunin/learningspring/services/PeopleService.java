package com.aryunin.learningspring.services;

import com.aryunin.learningspring.models.Person;
import com.aryunin.learningspring.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository repository;

    @Autowired
    public PeopleService(PeopleRepository repository) {
        this.repository = repository;
    }

    public List<Person> findAll() {
        return repository.findAll();
    }

    public Optional<Person> findById(int id) {
        Optional<Person> person = repository.findById(id);
        person.ifPresent(value -> Hibernate.initialize(value.getBooks()));
        return person;
    }

    public Optional<Person> findByName(String name) {
        Optional<Person> person = repository.findByName(name);
        person.ifPresent(value -> Hibernate.initialize(value.getBooks()));
        return person;
    }

    @Transactional
    public void save(Person person) {
        repository.save(person);
    }

    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }

    @Transactional
    public void update(int id, Person updated) {
        updated.setId(id);
        repository.save(updated);
    }
}
