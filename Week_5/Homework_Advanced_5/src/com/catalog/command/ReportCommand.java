package com.catalog.command;

import com.catalog.model.Catalog;
import com.catalog.exception.CatalogException;

import freemarker.template.*;

import java.io.*;
import java.awt.Desktop;
import java.util.*;

public class ReportCommand implements Command {

    private Catalog catalog;

    public ReportCommand(Catalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public void execute() throws CatalogException {

        try {

            Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
            cfg.setDirectoryForTemplateLoading(new File("."));

            Template template = cfg.getTemplate("report.ftl");

            Map<String, Object> data = new HashMap<>();
            data.put("resources", catalog.getResources());

            File output = new File("report.html");

            try (Writer writer = new FileWriter(output)) {
                template.process(data, writer);
            }

            Desktop.getDesktop().browse(output.toURI());

        } catch (Exception e) {
            throw new CatalogException("Report failed", e);
        }
    }
}