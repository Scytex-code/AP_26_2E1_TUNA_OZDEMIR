package com.lab6ha.util;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.lab6ha.dao.GenreDAO;
import com.lab6ha.dao.MovieDAO;
import com.lab6ha.database.DatabaseConnection;
import com.lab6ha.model.Genre;
import com.lab6ha.model.Movie;

public class ReportGenerator {

    private MovieDAO movieDAO = new MovieDAO();
    private GenreDAO genreDAO = new GenreDAO();
    private Genre getGenreForMovie(int movieId) throws SQLException {
        // MovieGenreDAO ile ilişkili genre ID’sini al
        Integer genreId = new com.lab6ha.dao.MovieGenreDAO().getGenreIdForMovie(movieId);
        if (genreId == null) return null;
        return genreDAO.findById(genreId);
    }

    /**
     * Generates an HTML report and inserts data into SQL view.
     */
    public void generateHTMLReport(String filePath) throws SQLException, IOException {
        List<Movie> movies = movieDAO.findAll();

        // HTML oluştur
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("<html><head><title>Movie Report</title></head><body>");
            writer.write("<h1>Movies</h1><table border='1'>");
            writer.write("<tr><th>Title</th><th>Duration</th><th>Score</th><th>Genre</th></tr>");

            // SQL view temizle ve ekleme için connection
            try (Connection con = DatabaseConnection.getConnection()) {
                // Eğer view yoksa geçici tabloya insert için table oluştur (view doğrudan insert edilemez MySQL’de)
                String clearSQL = "DELETE FROM movie_report"; // movie_report view için bir base table gereklidir
                try {
                    con.createStatement().executeUpdate(clearSQL);
                } catch (SQLException e) {
                    // view table yoksa oluştur
                    String createTable = "CREATE TABLE IF NOT EXISTS movie_report (" +
                            "title VARCHAR(200), duration INT, score DOUBLE, genre VARCHAR(50))";
                    con.createStatement().executeUpdate(createTable);
                }

                String insertSQL = "INSERT INTO movie_report(title, duration, score, genre) VALUES (?,?,?,?)";
                try (PreparedStatement ps = con.prepareStatement(insertSQL)) {

                    for (Movie movie : movies) {
                        Genre genre = getGenreForMovie(movie.getId());
                        String genreName = genre != null ? genre.getName() : "N/A";

                        // HTML satırı
                        writer.write("<tr>");
                        writer.write("<td>" + movie.getTitle() + "</td>");
                        writer.write("<td>" + movie.getDuration() + "</td>");
                        writer.write("<td>" + movie.getScore() + "</td>");
                        writer.write("<td>" + genreName + "</td>");
                        writer.write("</tr>");

                        // SQL insert
                        ps.setString(1, movie.getTitle());
                        ps.setInt(2, movie.getDuration());
                        ps.setDouble(3, movie.getScore());
                        ps.setString(4, genreName);
                        ps.addBatch();
                    }

                    ps.executeBatch();
                }
            }

            writer.write("</table></body></html>");
        }
        
    }
}