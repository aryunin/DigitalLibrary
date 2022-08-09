package com.aryunin.DigitalLibraryBoot.util;

import com.aryunin.DigitalLibraryBoot.models.Person;
import com.aryunin.DigitalLibraryBoot.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Calendar;

@Component
public class PersonValidator implements Validator {
    final private PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if(peopleService.findByName(person.getName()).isPresent()) errors.rejectValue("name", "", "This name is already exists");

        Calendar calendar = Calendar.getInstance();
        if(person.getBirthYear() < 1900) errors.rejectValue("birthYear", "", "The year of birth should be later than 1900");
        else if(person.getBirthYear() > calendar.get(Calendar.YEAR)) errors.rejectValue("birthYear", "", "The year of birth should be earlier than current year");
    }
}
