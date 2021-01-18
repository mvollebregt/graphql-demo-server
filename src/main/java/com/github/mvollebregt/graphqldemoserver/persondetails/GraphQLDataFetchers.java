package com.github.mvollebregt.graphqldemoserver.persondetails;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.awt.print.Book;
import java.util.List;

@Component
public class GraphQLDataFetchers {

    private RestTemplate restTemplate = new RestTemplate();

    public Person getPersonById(DataFetchingEnvironment dataFetchingEnvironment) {
        String id = dataFetchingEnvironment.getArgument("id");
        ResponseEntity<Person> entity = restTemplate.getForEntity("https://swapi.dev/api/people/" + id + "/", Person.class);
        return entity.getBody();
    }

    public Film getFilm(DataFetchingEnvironment dataFetchingEnvironment) {
        Person person = dataFetchingEnvironment.getSource();
        return person.getFilms().stream()
                .findFirst()
                .map(id -> restTemplate.getForEntity(id.replace("http://", "https://"), Film.class).getBody())
                .orElse(null);
    }
}
