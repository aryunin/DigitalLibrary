package com.aryunin.learningspring.services;

import com.aryunin.learningspring.models.Book;
import com.aryunin.learningspring.models.Person;
import com.aryunin.learningspring.repositories.BooksRepository;
import com.aryunin.learningspring.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BooksService(BooksRepository repository, PeopleRepository peopleRepository) {
        this.booksRepository = repository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public List<Book> findPage(int page, int booksPerPage) {
        return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public Optional<Book> findById(int id) {
        return booksRepository.findById(id);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updated) {
        updated.setId(id);
        booksRepository.save(updated);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void assign(int bookId, int personId) {
        Optional<Book> book = booksRepository.findById(bookId);
        // Если книга не найдена, то будет выброшено исключение
        if(!book.isPresent()) throw new RuntimeException("The book for assigning was not found");
        // Если книга найдена, то берем ссылку на человека с указанным ID (запрос к БД не за чем)
        Person person = peopleRepository.getReferenceById(personId);
        // Если человек с данным ID существует, то книгу получится ему присвоить, иначе сработает ограничение на БД
        book.get().setHolder(person);
        // Если ограничение вторичного ключа на БД не сработало, значит человек существует, и ему можно присвоить книгу
        person.getBooks().add(book.get());
    }

    @Transactional
    public void release(int id) {
        Optional<Book> book = booksRepository.findById(id);
        if(!book.isPresent()) throw new RuntimeException("The book for releasing was not found");
        book.get().getHolder().getBooks().remove(book.get());
        book.get().setHolder(null);
    }
}