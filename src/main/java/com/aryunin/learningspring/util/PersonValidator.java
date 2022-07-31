package com.aryunin.learningspring.util;

import com.aryunin.learningspring.models.Book;
import com.aryunin.learningspring.models.Person;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Calendar;

@Component
public class PersonValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        Calendar calendar = Calendar.getInstance();

        if(person.getBirthYear() < 1900) errors.rejectValue("birthYear", "", "The year of birth should be later than 1900");
        else if(person.getBirthYear() > calendar.get(Calendar.YEAR)) errors.rejectValue("birthYear", "", "The year of birth should be earlier than current year");
    }
}
