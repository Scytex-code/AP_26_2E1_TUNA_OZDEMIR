package com.catalog.command;

import com.catalog.exception.CatalogException;

public interface Command {
    void execute() throws CatalogException;
}