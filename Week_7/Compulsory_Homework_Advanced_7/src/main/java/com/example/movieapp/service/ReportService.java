package com.example.movieapp.service;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.movieapp.dao.MovieDAO;
import com.example.movieapp.model.Movie;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ReportService {

    private final MovieDAO movieDAO = new MovieDAO();

    public void generateMovieReport(String outputPath) throws SQLException, IOException, TemplateException {
        List<Movie> movies = movieDAO.findAll();

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setClassForTemplateLoading(ReportService.class, "/");
        cfg.setDefaultEncoding("UTF-8");

        Template template = cfg.getTemplate("movie_report.ftl");

        Map<String, Object> data = new HashMap<>();
        data.put("movies", movies);

        try (FileWriter writer = new FileWriter(outputPath)) {
            template.process(data, writer);
        }
    }
}