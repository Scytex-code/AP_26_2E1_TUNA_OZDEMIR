package com.catalog.command;

import com.catalog.model.Catalog;
import com.catalog.exception.CatalogException;

import java.io.*;

public class SaveCommand implements Command {

    private Catalog catalog;
    private String file;

    public SaveCommand(Catalog catalog, String file){
        this.catalog = catalog;
        this.file = file;
    }

    public void execute() throws CatalogException {

        try(ObjectOutputStream out =
            new ObjectOutputStream(new FileOutputStream(file))){

            out.writeObject(catalog);

        }catch(Exception e){
            throw new CatalogException("Save failed", e);
        }
    }
}