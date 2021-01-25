package com.github.mvollebregt.graphqldemoserver.persondetails;

import javax.persistence.*;
import java.util.List;

@Entity
public class Film {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String director;

    @ManyToMany()
    private List<Person> characters;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public List<Person> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Person> characters) {
        this.characters = characters;
    }
}
