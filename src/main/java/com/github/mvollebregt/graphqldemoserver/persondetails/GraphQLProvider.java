package com.github.mvollebregt.graphqldemoserver.persondetails;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider {

    private GraphQL graphQL;

    @Autowired
    GraphQLDataFetchers graphQLDataFetchers;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        Resource resource = new ClassPathResource("schema.graphqls");
        String sdl = Files.readString(resource.getFile().toPath());
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("allPeople", graphQLDataFetchers::getAllPeople)
                        .dataFetcher("allFilms", graphQLDataFetchers::getAllFilms)
                        .dataFetcher("personById", graphQLDataFetchers::getPersonById)
                        .dataFetcher("filmById", graphQLDataFetchers::getFilmById))
                .type(newTypeWiring("Mutation")
                        .dataFetcher("createPerson", graphQLDataFetchers::createPerson))
                .type(newTypeWiring("Person")
                        .dataFetcher("films", graphQLDataFetchers::getFilms))
                .type(newTypeWiring("Film")
                        .dataFetcher("people", graphQLDataFetchers::getPeople))
                .build();
    }
}
