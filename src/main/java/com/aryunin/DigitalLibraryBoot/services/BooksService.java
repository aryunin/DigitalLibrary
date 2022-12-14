package com.aryunin.DigitalLibraryBoot.services;

import com.aryunin.DigitalLibraryBoot.models.Book;
import com.aryunin.DigitalLibraryBoot.models.Person;
import com.aryunin.DigitalLibraryBoot.repositories.BooksRepository;
import com.aryunin.DigitalLibraryBoot.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;

    public void checkOverdue(Book book) {
        final long EXPIRATION_DAYS = 10; // days
        final long MILLISECONDS_PER_DAY = 86400000L;
        Date currentTime = new Date();
        if(currentTime.getTime() - book.getAssignedAt().getTime()
                > EXPIRATION_DAYS * MILLISECONDS_PER_DAY) book.setOverdue(true);
    }

    @Autowired
    public BooksService(BooksRepository repository, PeopleRepository peopleRepository) {
        this.booksRepository = repository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public List<Book> findAll(boolean sortByYear) {
        return (sortByYear) ? booksRepository.findAll(Sort.by("publicationYear")) : findAll();
    }

    public List<Book> findPage(int page, int booksPerPage) {
        return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public List<Book> findPage(int page, int booksPerPage, boolean sortByYear) {
        return (sortByYear) ? booksRepository
                .findAll(PageRequest.of(page, booksPerPage, Sort.by("publicationYear")))
                .getContent() : findPage(page, booksPerPage);
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
        // ???????? ?????????? ???? ??????????????, ???? ?????????? ?????????????????? ????????????????????
        if(!book.isPresent()) throw new RuntimeException("The book for assigning was not found");
        // ???????? ?????????? ??????????????, ???? ?????????? ???????????? ???? ???????????????? ?? ?????????????????? ID (???????????? ?? ???? ???? ???? ??????)
        Person person = peopleRepository.getReferenceById(personId);
        // ???????? ?????????????? ?? ???????????? ID ????????????????????, ???? ?????????? ?????????????????? ?????? ??????????????????, ?????????? ?????????????????? ?????????????????????? ???? ????
        book.get().setHolder(person);
        // ?????????? ?????????????????????????? ?????????? ???????????? ??????????
        book.get().setAssignedAt(new Date());
        // ???????? ?????????????????????? ???????????????????? ?????????? ???? ???? ???? ??????????????????, ???????????? ?????????????? ????????????????????, ?? ?????? ?????????? ?????????????????? ??????????
        person.getBooks().add(book.get());
    }

    @Transactional
    public void release(int id) {
        Optional<Book> book = booksRepository.findById(id);
        if(!book.isPresent()) throw new RuntimeException("The book for releasing was not found");
        book.get().getHolder().getBooks().remove(book.get());
        book.get().setHolder(null);
        book.get().setAssignedAt(null);
    }

    public List<Book> findByTitleStartingWith(String startingWith) {
        return booksRepository.findByTitleStartingWith(startingWith);
    }
}
