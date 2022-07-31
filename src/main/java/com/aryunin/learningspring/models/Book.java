package com.aryunin.learningspring.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Book {
    @NotNull(message = "The title can't be null")
    @Size(min=2, max = 255, message = "Incorrect title length")
    private String title;
    @NotNull(message = "The book must have an author")
    @Size(min=2, max = 255, message = "Incorrect author's name length")
    private String author;
    private int publicationYear;

    public Book(String title, String author, int publicationYear) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }
}
