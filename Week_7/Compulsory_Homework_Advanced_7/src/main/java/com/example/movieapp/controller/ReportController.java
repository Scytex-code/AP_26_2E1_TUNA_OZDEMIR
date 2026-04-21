package com.example.movieapp.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieapp.service.HtmlReportService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final HtmlReportService htmlReportService;

    public ReportController(HtmlReportService htmlReportService) {
        this.htmlReportService = htmlReportService;
    }

    @GetMapping(value = "/movies/html", produces = MediaType.TEXT_HTML_VALUE)
    @Operation(summary = "Generate the HTML report for all movies")
    public ResponseEntity<String> getMovieReport() {
        return ResponseEntity.ok(htmlReportService.buildMovieReportHtml());
    }
}
