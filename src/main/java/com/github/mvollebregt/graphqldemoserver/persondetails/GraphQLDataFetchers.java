package com.github.mvollebregt.graphqldemoserver.persondetails;

import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Component
public class GraphQLDataFetchers {

    private final FilmRepository filmRepository;
    private final PersonRepository personRepository;

    private static class PeopleResultList extends ResultList<Person> {
    }

    private static class FilmResultList extends ResultList<Film> {
    }

    @Autowired
    public GraphQLDataFetchers(FilmRepository filmRepository, PersonRepository personRepository) {
        this.filmRepository = filmRepository;
        this.personRepository = personRepository;
    }

    public Person createPerson(DataFetchingEnvironment environment) {
        Map<String, Object> personInput = environment.getArgument("person");
        Person person = new Person();
        person.setName((String) personInput.get("name"));
        person.setHeight((Integer) personInput.get("height"));
        return personRepository.save(person);
    }

    public List<Person> getAllPeople(DataFetchingEnvironment dataFetchingEnvironment) {
        return this.personRepository.findAll();
    }

    public List<Film> getAllFilms(DataFetchingEnvironment dataFetchingEnvironment) {
        return this.filmRepository.findAll();
    }

    public Person getPersonById(DataFetchingEnvironment dataFetchingEnvironment) {
        return this.personRepository.getOne(getId(dataFetchingEnvironment));
    }

    public Film getFilmById(DataFetchingEnvironment dataFetchingEnvironment) {
        return this.filmRepository.getOne(getId(dataFetchingEnvironment));
    }

    public List<Film> getFilms(DataFetchingEnvironment dataFetchingEnvironment) {
        return ((Person) dataFetchingEnvironment.getSource()).getFilms();
    }

    public List<Person> getPeople(DataFetchingEnvironment dataFetchingEnvironment) {
        return ((Film) dataFetchingEnvironment.getSource()).getCharacters();
    }

    private Long getId(DataFetchingEnvironment dataFetchingEnvironment) {
        try {
            return Long.parseLong(dataFetchingEnvironment.getArgument("id"));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
