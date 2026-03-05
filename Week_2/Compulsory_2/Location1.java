public class Location1 {
    private String name;
    private String type; 
    private double x, y;

    public Location1(String name, String type, double x, double y) {
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getX() { return x; }
    public double getY() { return y; }

    @Override
    public String toString() {
        return name + " (" + type + ") at [" + x + ", " + y + "]";
    }
}