package com.aryunin.learningspring.controllers;

import com.aryunin.learningspring.dao.BookDAO;
import com.aryunin.learningspring.models.Book;
import com.aryunin.learningspring.models.Person;
import com.aryunin.learningspring.services.PeopleService;
import com.aryunin.learningspring.util.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {
    final private BookDAO bookDAO;
    final private BookValidator bookValidator;
    final private PeopleService peopleService;

    @Autowired
    public BooksController(BookDAO bookDAO, BookValidator bookValidator, PeopleService peopleService) {
        this.bookDAO = bookDAO;
        this.bookValidator = bookValidator;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("books", bookDAO.getAllBooks());
        return "books/index";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if(bindingResult.hasErrors()) return "books/new";
        bookDAO.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        Optional<Book> book = bookDAO.get(id);
        if(!book.isPresent()) return "redirect:/books";
        model.addAttribute("book", book.get());

        Optional<Person> bookOwner = Optional.ofNullable(book.get().getHolder());
        bookOwner.ifPresent(value -> model.addAttribute("owner", value));

        model.addAttribute("people", peopleService.findAll());

        return "books/show";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Book> book = bookDAO.get(id);
        if(!book.isPresent()) return "redirect:/books";
        model.addAttribute("book", book.get());
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("person") @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if(bindingResult.hasErrors()) return "books/edit";
        bookDAO.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookDAO.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person person){
        bookDAO.assign(id, person.getId());
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id){
        bookDAO.release(id);
        return "redirect:/books";
    }
}
