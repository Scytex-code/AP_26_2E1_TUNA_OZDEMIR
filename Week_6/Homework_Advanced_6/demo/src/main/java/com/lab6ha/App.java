package com.lab6ha;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.lab6ha.dao.MovieListDAO;
import com.lab6ha.model.Movie;
import com.lab6ha.util.CSVImporter;
import com.lab6ha.util.MovieListGenerator;
import com.lab6ha.util.ReportGenerator;

public class App {

    public static void main(String[] args) {
        System.out.println("=== JDBC Movie App Starting ===");

        CSVImporter importer = new CSVImporter();
        ReportGenerator reportGen = new ReportGenerator();
        MovieListGenerator listGen = new MovieListGenerator();
        MovieListDAO movieListDAO = new MovieListDAO();

        String csvPath = "C:/Users/tunao/OneDrive/Masaüstü/AP26/Week_6/Homework_Advanced_6/demo/src/main/java/com/lab6ha/data/movies.csv";
        String reportPath = "reports/movie_report.html";

        File reportDir = new File("reports");
        if (!reportDir.exists()) reportDir.mkdirs();

        try {
            // 1️⃣ CSV’den filmleri import et
            importer.importMovies(csvPath);
            System.out.println("Movies imported successfully from CSV.");

            // 2️⃣ HTML raporu oluştur
            reportGen.generateHTMLReport(reportPath);
            System.out.println("HTML report generated at " + reportPath);

            // 3️⃣ Unrelated movie lists üret ve DB’ye kaydet
            List<List<Movie>> movieLists = listGen.generateUnrelatedLists();
            System.out.println("Generated " + movieLists.size() + " unrelated movie lists:");

            for (int i = 0; i < movieLists.size(); i++) {
                List<Movie> list = movieLists.get(i);
                String listName = "Movie List " + (i + 1);

                // movie_list tablosuna ekle ve ID al
                int listId = movieListDAO.create(listName);

                System.out.print(listName + ": ");
                for (Movie m : list) {
                    System.out.print(m.getTitle() + ", ");
                    // movie_list_movies tablosuna ekle
                    movieListDAO.addMovieToList(listId, m.getId());
                }
                System.out.println();
            }

            // 4️⃣ HTML raporu aç
            File htmlFile = new File(reportPath);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(htmlFile.toURI());
                System.out.println("HTML report opened in default browser.");
            } else {
                System.out.println("Desktop not supported. Open the report manually.");
            }

        } catch (IOException | SQLException e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== JDBC Movie App Finished ===");
    }
}