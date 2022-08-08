package com.aryunin.learningspring.services;

import com.aryunin.learningspring.models.Book;
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
    private final PeopleRepository peopleRepository;
    private final BooksService booksService;

    private void checkBooksOverdue(List<Book> books) {
        for (Book book : books) booksService.checkOverdue(book);
    }

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BooksService booksService) {
        this.peopleRepository = peopleRepository;
        this.booksService = booksService;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Optional<Person> findById(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        person.ifPresent(value -> checkBooksOverdue(value.getBooks()));
        return person;
    }

    public Optional<Person> findByName(String name) {
        Optional<Person> person = peopleRepository.findByName(name);
        person.ifPresent(value -> Hibernate.initialize(value.getBooks()));
        return person;
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Person updated) {
        updated.setId(id);
        peopleRepository.save(updated);
    }
}
