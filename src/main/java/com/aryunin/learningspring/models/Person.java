package com.aryunin.learningspring.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Person {
    private int id;
    @NotNull(message = "The name can't be null")
    @Size(min=2, max = 255, message = "Incorrect name length")
    private String name;
    private int birthYear;

    public Person() {
    }

    public Person(int id, String name, int birthYear) {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
