package com.catalog.command;

import com.catalog.model.Catalog;
import com.catalog.model.Resource;

public class ListCommand implements Command {

    private Catalog catalog;

    public ListCommand(Catalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public void execute() {
        for (Resource r : catalog.getResources()) {
            System.out.println(r);
        }
    }
}