package main;

import model.Resource;
import repository.Catalog;
import exception.InvalidResourceException;

public class Main {
    public static void main(String[] args) {
        Main app = new Main();
        try {
            app.testCreateAdd();
        } catch (InvalidResourceException e) {
            System.err.println("Bir hata oluştu: " + e.getMessage());
        }
    }

    private void testCreateAdd() throws InvalidResourceException {
        Catalog catalog = new Catalog("My References");

        Resource res1 = new Resource("knuth67", "The Art of Computer Programming", 
                                     "d:/books/programming/tacp.ps", 1967, "Donald E. Knuth");
        Resource res2 = new Resource("java25", "The Java Language Specification", 
                                     "https://docs.oracle.com/javase/specs/jls/se25/jls25.pdf", 2025, "James Gosling");

        catalog.add(res1);
        catalog.add(res2);

        System.out.println("Katalog içeriği: " + catalog);

        // Bir kaynağı açmayı dene (Örn: Web adresi olan)
        System.out.println("Kaynak açılıyor...");
        catalog.view(res2);
    }
}