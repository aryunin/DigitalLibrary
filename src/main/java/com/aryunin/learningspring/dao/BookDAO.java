package com.aryunin.learningspring.dao;

import com.aryunin.learningspring.models.Book;
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
public class BookDAO {
    final private SessionFactory sessionFactory;

    @Autowired
    public BookDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select b from Book b", Book.class).getResultList();
    }

    @Transactional
    public void save(Book book) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(book);
    }

    @Transactional(readOnly = true)
    public Optional<Book> get(int id) {
        Session session = sessionFactory.getCurrentSession();
        Optional<Book> book = Optional.ofNullable(session.get(Book.class, id));
        book.ifPresent(value-> Hibernate.initialize(value.getHolder()));
        return book;
    }

    @Transactional
    public void update(int id, Book book) {
        Session session = sessionFactory.getCurrentSession();
        Book b = session.get(Book.class, id);
        b.setAuthor(book.getAuthor());
        b.setTitle(book.getTitle());
        b.setPublicationYear(book.getPublicationYear());
    }

    @Transactional
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(session.get(Book.class, id));
    }

    @Transactional
    public void assign(int bookId, int personId) {
        Session session = sessionFactory.getCurrentSession();
        Book b = session.get(Book.class, bookId);
        Person p = session.get(Person.class, personId);
        b.setHolder(p);
        p.getBooks().add(b);
    }

    @Transactional
    public void release(int id) {
        Session session = sessionFactory.getCurrentSession();
        Book b = session.get(Book.class, id);
        b.getHolder().getBooks().remove(b);
        b.setHolder(null);
    }
}
