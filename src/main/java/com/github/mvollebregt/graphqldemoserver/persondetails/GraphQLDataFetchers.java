package com.github.mvollebregt.graphqldemoserver.persondetails;

import graphql.schema.DataFetchingEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Component
public class GraphQLDataFetchers {

    private static class PeopleResultList extends ResultList<Person> {
    }

    private static class FilmResultList extends ResultList<Film> {
    }

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Person> getAllPeople(DataFetchingEnvironment dataFetchingEnvironment) {
        return getAll("people", PeopleResultList.class).getResults();
    }

    public List<Film> getAllFilms(DataFetchingEnvironment dataFetchingEnvironment) {
        return getAll("films", FilmResultList.class).getResults();
    }

    public Person getPersonById(DataFetchingEnvironment dataFetchingEnvironment) {
        return getOne(dataFetchingEnvironment, "people", Person.class);
    }

    public Film getFilmById(DataFetchingEnvironment dataFetchingEnvironment) {
        return getOne(dataFetchingEnvironment, "films", Film.class);
    }

    public List<Film> getFilms(DataFetchingEnvironment dataFetchingEnvironment) {
        System.out.println("start getFilms");
        List<Film> sub = getSub(dataFetchingEnvironment, Person::getFilms, Film.class);
        System.out.println("end getFilms");
        return sub;
    }

    public List<Person> getPeople(DataFetchingEnvironment dataFetchingEnvironment) {
        System.out.println("start getPeople");
        List<Person> sub = getSub(dataFetchingEnvironment, Film::getCharacters, Person.class);
        System.out.println("end getPeople");
        return sub;
    }

    public <T> T getAll(String endpoint, Class<T> clazz) {
        ResponseEntity<T> entity = restTemplate.getForEntity("https://swapi.dev/api/" + endpoint + "/", clazz);
        return entity.getBody();
    }

    public <T> T getOne(DataFetchingEnvironment dataFetchingEnvironment, String endpoint, Class<T> clazz) {
        String id = dataFetchingEnvironment.getArgument("id");
        ResponseEntity<T> entity = restTemplate.getForEntity("https://swapi.dev/api/" + endpoint + "/" + id + "/", clazz);
        return entity.getBody();
    }

    public <T, S> List<S> getSub(DataFetchingEnvironment dataFetchingEnvironment, Function<T, List<String>> property, Class<S> clazz) {
        T obj = dataFetchingEnvironment.getSource();
        return property.apply(obj).stream()
                .map(id -> restTemplate.getForEntity(id.replace("http://", "https://"), clazz).getBody()).collect(toList());
    }
}
