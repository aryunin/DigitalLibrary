package com.aryunin.learningspring.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Person {
    @NotNull(message = "The name can't be null")
    @Size(min=2, max = 255, message = "Incorrect name length")
    private String name;
    private int birthYear;

    public Person() {
    }

    public Person(String name, int birthYear) {
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
}
