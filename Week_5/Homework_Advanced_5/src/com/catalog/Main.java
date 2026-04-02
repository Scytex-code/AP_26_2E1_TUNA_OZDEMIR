package com.catalog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.catalog.algorithm.SetCoverSolver;
import com.catalog.command.Command;
import com.catalog.command.ListCommand;
import com.catalog.command.LoadCommand;
import com.catalog.command.ReportCommand;
import com.catalog.command.SaveCommand;
import com.catalog.model.Catalog;
import com.catalog.model.Resource;

public class Main {

    public static void main(String[] args) throws Exception {

        Catalog catalog = new Catalog();

        catalog.add(new Resource(
                "knuth67",
                "The Art of Computer Programming",
                "https://example.com/book",
                "Donald Knuth",
                1967,
                Arrays.asList("Algorithms","OOP")
        ));

        catalog.add(new Resource(
                "jvm25",
                "Java Virtual Machine Specification",
                "https://docs.oracle.com/javase/specs/jvms/se25/html/index.html",
                "Tim Lindholm",
                2025,
                Arrays.asList("OOP")
        ));

        catalog.add(new Resource(
                "ai01",
                "Neural Networks Basics",
                "https://example.com/ai",
                "Andrew Ng",
                2020,
                Arrays.asList("Neural Networks")
        ));

        catalog.add(new Resource(
                "graph01",
                "Graph Theory Introduction",
                "https://example.com/graph",
                "Some Author",
                2018,
                Arrays.asList("Graph theory")
        ));


        // LIST COMMAND
        Command list = new ListCommand(catalog);
        list.execute();


        // REPORT COMMAND
        Command report = new ReportCommand(catalog);
        report.execute();


        // SAVE COMMAND
        Command save = new SaveCommand(catalog,"catalog.dat");
        save.execute();


        // LOAD COMMAND
        LoadCommand load = new LoadCommand("catalog.dat");
        load.execute();

        Catalog loadedCatalog = load.getCatalog();


        System.out.println("\nLoaded catalog:");
        Command list2 = new ListCommand(loadedCatalog);
        list2.execute();



        // ===============================
        // ADVANCED PART
        // ===============================

        System.out.println("\nADVANCED: Concept Coverage");


        Set<String> concepts = new HashSet<>();

        concepts.add("Graph theory");
        concepts.add("Neural Networks");
        concepts.add("Algorithms");
        concepts.add("OOP");


        List<Resource> solution =
                SetCoverSolver.solve(loadedCatalog.getResources(), concepts);


        System.out.println("\nResources covering all concepts:");

        for(Resource r : solution)
            System.out.println(r.getTitle());


        // RANDOM TEST (performance test)

        System.out.println("\nRandom instance test:");

        Random rand = new Random();

        List<Resource> randomResources = new ArrayList<>();

        String[] keywords = {
                "Graph theory",
                "Neural Networks",
                "Algorithms",
                "OOP"
        };


        for(int i=0;i<20;i++){

            List<String> kw = new ArrayList<>();

            for(String k : keywords)
                if(rand.nextBoolean())
                    kw.add(k);

            if(kw.isEmpty())
                kw.add(keywords[rand.nextInt(keywords.length)]);

            randomResources.add(new Resource(
                    "r"+i,
                    "Random Resource "+i,
                    "https://example.com/"+i,
                    "Random Author",
                    2024,
                    kw
            ));
        }

        List<Resource> randomSolution =
                SetCoverSolver.solve(randomResources, concepts);

        System.out.println("\nRandom instance - Minimum cover resources:");
        for(Resource r : randomSolution){
                System.out.println("ID: " + r.getId());
                System.out.println("Title: " + r.getTitle());
                System.out.println("Author: " + r.getAuthor());
                System.out.println("Year: " + r.getYear());
                System.out.println("Location: " + r.getLocation());
                System.out.println("Keywords: " + String.join(", ", r.getKeywords()));
                System.out.println("-----------------------------------");
        }

        System.out.println("\nRandom cover size: "+randomSolution.size());

    }
}