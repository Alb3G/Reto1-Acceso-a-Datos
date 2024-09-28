import com.reto.MovieUtils;
import com.reto.model.Movie;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MovieUtilsTest {
    @Test
    public void getMoviesFromCsvOk() {
        ArrayList<Movie> movies = MovieUtils.getMoviesFromCsv("peliculasTest.csv", new ArrayList<>(List.of("all")));
        assertEquals(30, movies.size()); // Method should read 30 films from the csv file.
    }

    @Test
    public void loadHtmlTemplateOk() {
        StringBuilder sb = new StringBuilder();

        MovieUtils.loadHtmlTemplate(sb);
        assertFalse(sb.isEmpty()); // It should show false that means sb has the template in it.
    }

    @Test
    public void writeMoviesIntoTemplateOk() throws IOException {
        ArrayList<Movie> movies = MovieUtils.getMoviesFromCsv("peliculasTest.csv", new ArrayList<>(List.of("all")));

        StringBuilder sb = new StringBuilder();

        MovieUtils.loadHtmlTemplate(sb);

        MovieUtils.writeMoviesIntoTemplate(sb, movies);

        // We check that the specific file contained in the list exists in the resources directory
        for(Movie movie : movies) {
            File movieFile = new File("src/main/output/" + movie.getTitle() + "-" + movie.getId() +  ".html");
            assertTrue(movieFile.exists());
        }

        for(Movie movie : movies) {
            Files.deleteIfExists(Path.of("src/main/output/" + movie.getTitle() + "-" + movie.getId() +  ".html"));
        }
        File dir = new File("src/main/output");
        dir.delete();
    }
}
