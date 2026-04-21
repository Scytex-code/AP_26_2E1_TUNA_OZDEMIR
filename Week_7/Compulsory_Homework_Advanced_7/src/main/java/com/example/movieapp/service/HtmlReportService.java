package com.example.movieapp.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieapp.dto.MovieResponse;
import com.example.movieapp.exception.BadRequestException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class HtmlReportService {

    private final MovieService movieService;
    private final Configuration configuration;

    public HtmlReportService(MovieService movieService, Configuration configuration) {
        this.movieService = movieService;
        this.configuration = configuration;
    }

    @Transactional(readOnly = true)
    public String buildMovieReportHtml() {
        try (StringWriter writer = new StringWriter()) {
            Template template = configuration.getTemplate("movie_report.ftl");
            Map<String, Object> model = new HashMap<>();
            model.put("movies", movieService.getAllMovies());
            template.process(model, writer);
            return writer.toString();
        } catch (IOException | TemplateException exception) {
            throw new BadRequestException("Could not generate movie report: " + exception.getMessage());
        }
    }
}
