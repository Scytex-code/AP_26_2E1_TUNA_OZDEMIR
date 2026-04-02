package com.catalog.command;

import com.catalog.model.Catalog;
import com.catalog.exception.CatalogException;

import java.io.*;

public class LoadCommand implements Command {

    private String file;
    private Catalog catalog;

    public LoadCommand(String file){
        this.file = file;
    }

    public Catalog getCatalog(){
        return catalog;
    }

    public void execute() throws CatalogException {

        try(ObjectInputStream in =
            new ObjectInputStream(new FileInputStream(file))){

            catalog = (Catalog) in.readObject();

        }catch(Exception e){
            throw new CatalogException("Load failed", e);
        }
    }
}