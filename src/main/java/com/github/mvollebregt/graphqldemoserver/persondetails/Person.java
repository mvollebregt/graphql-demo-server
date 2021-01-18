package com.github.mvollebregt.graphqldemoserver.persondetails;

import java.util.List;

public class Person {

    private String name;
    private int height;
    private List<String> films;

    public List<String> getFilms() {
        return films;
    }

    public void setFilms(List<String> films) {
        this.films = films;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
