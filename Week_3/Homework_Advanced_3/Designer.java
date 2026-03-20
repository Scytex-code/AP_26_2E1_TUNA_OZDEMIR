// Designer.java
import java.time.LocalDate;

public class Designer extends Person {
    private String designTool; // Specific property (e.g., Figma, Photoshop)

    public Designer(int id, String name, LocalDate birthDate, String tool) {
        super(id, name, birthDate);
        this.designTool = tool;
    }

    @Override
    public String getContactDetails() {
        return "Primary Tool: " + designTool;
    }
}