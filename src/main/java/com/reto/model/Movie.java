package com.reto.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class that represents a Movie entity
 * The @data tag allow us to use an empty constructor
 * @author Alberto Guzmán
 */
@Data
@AllArgsConstructor
public class Movie {
    private String id;
    private String title;
    private Integer year; // It represents the release year of the movie
    private String director;
    private String genre;
}
