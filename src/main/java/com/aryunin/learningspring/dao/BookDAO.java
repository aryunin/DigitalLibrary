package com.aryunin.learningspring.dao;

import com.aryunin.learningspring.models.Book;
import com.aryunin.learningspring.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    final private JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> getAllBooks() {
        return jdbcTemplate.query("SELECT book_id AS id, title, author, publication_year FROM book", new BeanPropertyRowMapper<>(Book.class));
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO book(title, author, publication_year) values(?, ?, ?)", book.getTitle(), book.getAuthor(), book.getPublicationYear());
    }

    public Optional<Book> get(int id) {
        return jdbcTemplate.query("SELECT book_id AS id, title, author, publication_year FROM book WHERE book_id=?", new BeanPropertyRowMapper<>(Book.class), id).stream().findAny();
    }

    public void update(int id, Book book) {
        jdbcTemplate.update("UPDATE book SET title=?, author=?, publication_year=? WHERE book_id=?",
                book.getTitle(), book.getAuthor(), book.getPublicationYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM book WHERE book_id=?", id);
    }

    public Optional<Person> getOwner(int bookId) {
        return jdbcTemplate.query("SELECT person.* FROM person JOIN book ON person.person_id = book.person_id WHERE book.book_id=?",
                new BeanPropertyRowMapper<>(Person.class), bookId).stream().findAny();
    }

    public void assign(int bookId, int personId) {
        jdbcTemplate.update("UPDATE book SET person_id=? WHERE book_id=?", personId, bookId);
    }

    public void release(int id) {
        jdbcTemplate.update("UPDATE book SET person_id=? WHERE book_id=?", null, id);
    }
}
