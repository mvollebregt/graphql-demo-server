package com.github.mvollebregt.graphqldemoserver.persondetails;

import java.util.List;

public class ResultList<T> {

    private List<T> results;

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
