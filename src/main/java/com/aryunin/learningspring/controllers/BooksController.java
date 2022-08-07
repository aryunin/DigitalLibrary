package com.aryunin.learningspring.controllers;

import com.aryunin.learningspring.models.Book;
import com.aryunin.learningspring.models.Person;
import com.aryunin.learningspring.services.BooksService;
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
    final private BooksService booksService;
    final private BookValidator bookValidator;
    final private PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, BookValidator bookValidator, PeopleService peopleService) {
        this.booksService = booksService;
        this.bookValidator = bookValidator;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String index(Model model, @RequestParam Optional<Integer> page,
                        @RequestParam Optional<Integer> books_per_page,
                        @RequestParam Optional<Boolean> sort_by_year) {
        boolean sort = sort_by_year.orElse(false);
        if(page.isPresent() && books_per_page.isPresent())
            model.addAttribute("books", booksService.findPage(page.get(), books_per_page.get(), sort));
        else model.addAttribute("books", booksService.findAll(sort));
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
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        Optional<Book> book = booksService.findById(id);
        if(!book.isPresent()) return "redirect:/books";
        model.addAttribute("book", book.get());

        Optional<Person> bookOwner = Optional.ofNullable(book.get().getHolder());
        bookOwner.ifPresent(value -> model.addAttribute("owner", value));

        model.addAttribute("people", peopleService.findAll());

        return "books/show";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Book> book = booksService.findById(id);
        if(!book.isPresent()) return "redirect:/books";
        model.addAttribute("book", book.get());
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("person") @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if(bindingResult.hasErrors()) return "books/edit";
        booksService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person person){
        booksService.assign(id, person.getId());
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id){
        booksService.release(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam Optional<String> search_query) {
        search_query.ifPresent(s -> model.addAttribute("books", booksService.findByTitleStartingWith(s)));
        return "books/search";
    }
}
