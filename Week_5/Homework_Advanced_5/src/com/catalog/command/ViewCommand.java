package com.catalog.command;

import com.catalog.model.Catalog;
import com.catalog.model.Resource;
import com.catalog.exception.CatalogException;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;

public class ViewCommand implements Command {

    private Catalog catalog;
    private String id;

    public ViewCommand(Catalog catalog, String id) {
        this.catalog = catalog;
        this.id = id;
    }

    @Override
    public void execute() throws CatalogException {

        try {

            Resource r = catalog.findById(id);

            if (r == null)
                throw new CatalogException("Resource not found");

            Desktop desktop = Desktop.getDesktop();

            if (r.getLocation().startsWith("http"))
                desktop.browse(new URI(r.getLocation()));
            else
                desktop.open(new File(r.getLocation()));

        } catch (Exception e) {
            throw new CatalogException("View failed", e);
        }
    }
}