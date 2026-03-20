// Programmer.java
import java.time.LocalDate;

public class Programmer extends Person {
    private String favoriteLanguage; // Specific property

    public Programmer(int id, String name, LocalDate birthDate, String language) {
        super(id, name, birthDate);
        this.favoriteLanguage = language;
    }

    @Override
    public String getContactDetails() {
        return "Dev Language: " + favoriteLanguage;
    }
}