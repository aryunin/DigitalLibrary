package com.aryunin.DigitalLibraryBoot.util;

import com.aryunin.DigitalLibraryBoot.models.Book;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Calendar;

@Component
public class BookValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;

        Calendar calendar = Calendar.getInstance();

        if(book.getPublicationYear() < 1454) errors.rejectValue("publicationYear", "", "The year of publication should be later than 1454");
        else if(book.getPublicationYear() > calendar.get(Calendar.YEAR)) errors.rejectValue("publicationYear", "", "The year of publication should be earlier than current year");
    }
}
