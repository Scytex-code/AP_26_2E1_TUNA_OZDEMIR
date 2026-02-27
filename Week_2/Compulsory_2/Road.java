public class Road {
    private String type;
    private double length;
    private int speedLimit;
    private Location source;
    private Location destination;

    public Road(String type, double length, int speedLimit, Location source, Location destination) {
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

    public Location getSource() { return source; }
    public void setSource(Location source) { this.source = source; }

    public Location getDestination() { return destination; }
    public void setDestination(Location destination) { this.destination = destination; }

    @Override
    public String toString() {
        return type + " Road: " + source.getName() + " -> " + destination.getName() + 
               " | Length: " + length + "km | Speed Limit: " + speedLimit + "km/h";
    }
}