package com.reto;

import com.reto.model.Movie;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


/**
 * This class encapsulate all the logic for processing the csv file
 * and for creating the html templates for each movie.
 * @author Alberto Guzmán
 */
public final class MovieUtils {
    /**
     * This method parse and map all the movies from the csv file and stores it in the ArrayList
     * that we will return later.
     * @param fileName -> Name of the file that we want to read, this param makes easier
     * to test the method later.
     * @param files -> List of Strings which contains the titles of the movies that we will create,
     * the titles must match the movie title EXACTLY or the film won't be created.
     * @return ArrayList<Movie> movies;
     */
    public static ArrayList<Movie> getMoviesFromCsv(String fileName, ArrayList<String> files) {
        ArrayList<Movie> movies = new ArrayList<>(0);
        try(
                BufferedReader bfr = new BufferedReader(new FileReader(fileName))
        ) {
            String line;
            while((line = bfr.readLine()) != null) {
                var lineParts = line.split(",");
                if(createSelectedFiles(files, lineParts[1])) {
                    movies.add(new Movie(
                            lineParts[0], // Id
                            lineParts[1], // Title
                            Integer.parseInt(lineParts[2]), // Year
                            lineParts[3], // Director
                            lineParts[4] // genre
                    ));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return movies;
    }

    /**
     * This method determines wether the file should be created or not.
     * @param files List of Strings which contains the titles of the movies that we will create
     * @param title Movie title that should be in the files list in order to be created
     * @return True or False to specify if the movie should be created or not
     */
    private static boolean createSelectedFiles(ArrayList<String> files, String title) {
        boolean res = false;
        int i = 0;
        while(i < files.size() && !res) {
            if(files.get(i).equals(title) || files.get(i).equals("all")) {
                res = true;
            }
            i++;
        }

        return res;
    }

    /**
     * Method that prints the info about how to use the app.
     * @param files List of Strings which contains the titles of the movies that we will create
     */
    public static void movieAppTerminalInfo(ArrayList<String> files) {
        Scanner sc = new Scanner(System.in);
        String movieTitle;

        System.out.println("Hi user welcome to MyMovieShelf.");
        System.out.println("The rules to use the app are so simple:");
        System.out.println("1.If you want to get all the films available type 'all'.");
        System.out.println("2.If you dont want to see any movie type 'none'.");
        System.out.println("3.And to see any specific movie just type the name");

        /*
            Matrix Tiburón Forrest Gump Pulp Fiction
         */

        do {
            System.out.println("Introduce a movie title:");
            movieTitle = sc.nextLine();
            files.add(movieTitle);
        }while(!movieTitle.equals("none") && !movieTitle.equals("all") && !movieTitle.equals("end"));

        if(movieTitle.equals("none")) {
            files.clear();
        }
    }

    /**
     * This method reads the template.html and stores it in a StringBuilder where we will replace
     * the placeholder atributes for the movie properties, if there's any parameter missing the sb will
     * be replaced by an empty string "".
     * @param sb The StringBuilder where the html template is stored.
     */
    public static void loadHtmlTemplate(StringBuilder sb) {
        try(
                BufferedReader bfr = new BufferedReader(new FileReader("template.html"))
        ) {
            String line;
            while((line = bfr.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
            if(!validateTemplate(String.valueOf(sb))) {
                sb.replace(0, sb.length(), "");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method checks wether the template is valid or not, it means that if any
     * of the placeholders are missing the template won't be generated, except with the
     * %%2%% cause there's 2 of them but it both of them are missing
     * @param template String that contains the html template the template won't be generated as well.
     * @return Boolean that means if the template has all the necessary parameters to
     * generate the html files
     */
    private static boolean validateTemplate(String template) {
        return template.contains("%%1%%") && template.contains("%%2%%") &&
                template.contains("%%3%%") && template.contains("%%4%%") &&
                template.contains("%%5%%");
    }

    /**
     * This method generates a html template with the properties of the movie param
     * that we pass to it, only if the template that receives is not empty.
     * @param template StringBuilder with html template stored coud be empty.
     * @param movies A Movie list to generate the template for each movie
     */
    public static void writeMoviesIntoTemplate(StringBuilder template, ArrayList<Movie> movies) {
        if(!template.toString().isEmpty()) {
            File dir = new File("src/main/output");
            checkIfDirExists(dir);
            for(Movie movie : movies) {
                try(
                        BufferedWriter bfw = new BufferedWriter(new FileWriter("src/main/output/" + movie.getTitle() + "-" + movie.getId() +  ".html"))
                ) {
                    String finalTemplate = template.toString()
                            .replace("%%1%%", movie.getId())
                            .replace("%%2%%", movie.getTitle())
                            .replace("%%3%%", movie.getYear().toString())
                            .replace("%%4%%", movie.getDirector())
                            .replace("%%5%%", movie.getGenre());
                    bfw.write(finalTemplate);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            System.out.println("Files coudn't be generated, due to a format error in template");
        }
    }

    /**
     * In this method we check if the file exists, and it's a dir, then we delete his
     * content (movie templates) and the dir, for create it again later.
     * @param dir Supposed to be the directory where the films will be created at.
     */
    private static void checkIfDirExists(File dir) {
        if(dir.exists() && dir.isDirectory()) {
            for(File file : Objects.requireNonNull(dir.listFiles())) {
                file.delete();
            }
            dir.delete();
        } else {
            dir.delete();
        }
        dir.mkdirs();
    }

}
