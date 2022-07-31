package com.aryunin.learningspring.controllers;

import com.aryunin.learningspring.dao.PersonDAO;
import com.aryunin.learningspring.models.Person;
import com.aryunin.learningspring.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PeopleController {
    final private PersonDAO personDAO;
    final private PersonValidator personValidator;

    @Autowired
    public PeopleController(PersonDAO personDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.personValidator = personValidator;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("people", personDAO.getAllPeople());
        return "people/index";
    }

    @GetMapping("/new")
    public String newPersonForm(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()) return "people/new";
        personDAO.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String showInfo(@PathVariable("id") int id, Model model) {
        Optional<Person> person = personDAO.get(id);
        if(!person.isPresent()) return "redirect:/people";
        else model.addAttribute("person", person.get());
        return "people/show";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Person> person = personDAO.get(id);
        if(!person.isPresent()) return "redirect:/people";
        model.addAttribute("person", person.get());
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasFieldErrors("birthYear")) return "people/edit";
        personDAO.update(id, person);
        return "redirect:/people";
    }
}
