package com.reto;

import com.reto.model.Movie;
import java.util.ArrayList;

import static com.reto.MovieUtils.movieAppTerminalInfo;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> files = new ArrayList<>();

        movieAppTerminalInfo(files);

        ArrayList<Movie> movies = MovieUtils.getMoviesFromCsv("peliculasTest.csv", files);

        StringBuilder sb = new StringBuilder();

        MovieUtils.loadHtmlTemplate(sb);

        MovieUtils.writeMoviesIntoTemplate(sb, movies);
    }
}
