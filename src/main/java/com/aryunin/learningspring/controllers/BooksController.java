package com.aryunin.learningspring.controllers;

import com.aryunin.learningspring.dao.BookDAO;
import com.aryunin.learningspring.models.Book;
import com.aryunin.learningspring.models.Person;
import com.aryunin.learningspring.util.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BooksController {
    final private BookDAO bookDAO;
    final private BookValidator bookValidator;

    @Autowired
    public BooksController(BookDAO bookDAO, BookValidator bookValidator) {
        this.bookDAO = bookDAO;
        this.bookValidator = bookValidator;
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
}
