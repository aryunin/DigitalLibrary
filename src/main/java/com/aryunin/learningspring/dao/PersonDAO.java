package com.aryunin.learningspring.dao;

import com.aryunin.learningspring.models.Person;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
//    final private SessionFactory sessionFactory;

//    @Autowired
//    public PersonDAO(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }

//    @Transactional(readOnly = true)
//    public List<Person> getAllPeople() {
//        Session session = sessionFactory.getCurrentSession();
//        return session.createQuery("select p from Person p", Person.class).getResultList();
//    }

//    @Transactional
//    public void save(Person person) {
//        Session session = sessionFactory.getCurrentSession();
//        session.persist(person);
//    }

//    @Transactional(readOnly = true)
//    public Optional<Person> get(String name) {
//        Session session = sessionFactory.getCurrentSession();
//        Optional<Person> person = session.createQuery("select p from Person p WHERE p.name=:name", Person.class).setParameter("name", name)
//                .getResultList().stream().findAny();
//        person.ifPresent(value -> Hibernate.initialize(value.getBooks()));
//        return person;
//    }
//
//    @Transactional(readOnly = true)
//    public Optional<Person> get(int id) {
//        Session session = sessionFactory.getCurrentSession();
//        Optional<Person> person = Optional.ofNullable(session.get(Person.class, id));
//        person.ifPresent(value -> Hibernate.initialize(value.getBooks()));
//        return person;
//    }

//    @Transactional
//    public void delete(int id) {
//        Session session = sessionFactory.getCurrentSession();
//        session.remove(session.get(Person.class, id));
//    }
//
//    @Transactional
//    public void update(int id, Person person) {
//        Session session = sessionFactory.getCurrentSession();
//        Person p = session.get(Person.class, id);
//        p.setName(person.getName());
//        p.setBirthYear(person.getBirthYear());
//    }
}
