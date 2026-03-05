public class Road1 {
    private String type;
    private double length;
    private int speedLimit;
    private Location1 source;
    private Location1 destination;

    public Road1(String type, double length, int speedLimit, Location1 source, Location1 destination) {
        this.type = type;
        this.speedLimit = speedLimit;
        this.source = source;
        this.destination = destination;

        double euclideanDistance = Math.sqrt(Math.pow(destination.getX() - source.getX(), 2) + Math.pow(destination.getY() - source.getY(), 2));

        if (length < euclideanDistance) {
            throw new IllegalArgumentException("Yol uzunluğu, konumlar arasındaki Öklid mesafesinden kısa olamaz!");
        }
        this.length = length;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getLength() { return length; }
    public void setLength(double length) { this.length = length; }

    public int getSpeedLimit() { return speedLimit; }
    public void setSpeedLimit(int speedLimit) { this.speedLimit = speedLimit; }

    public Location1 getSource() { return source; }
    public void setSource(Location1 source) { this.source = source; }

    public Location1 getDestination() { return destination; }
    public void setDestination(Location1 destination) { this.destination = destination; }

    @Override
    public String toString() {
        return type + " Road: " + source.getName() + " -> " + destination.getName() + 
               " | Length: " + length + "km | Speed Limit: " + speedLimit + "km/h";
    }
}