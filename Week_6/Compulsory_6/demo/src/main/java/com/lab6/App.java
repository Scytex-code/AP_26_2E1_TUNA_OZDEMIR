package com.lab6;

import com.lab6.dao.GenreDAO;

public class App {

    public static void main(String[] args) {

        try {

            GenreDAO genreDAO = new GenreDAO();

            genreDAO.create("Action");
            genreDAO.create("Drama");

            System.out.println(genreDAO.findByName("Action"));
            System.out.println(genreDAO.findById(1));

        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}