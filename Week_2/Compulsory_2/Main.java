public class Main {
    public static void main(String[] args) {
        Location loc1 = new Location("Istanbul", "City", 0, 0);
        Location loc2 = new Location("Ankara", "City", 400, 300);

        try {
            Road road1 = new Road("Highway", 550.0, 120, loc1, loc2);
            System.out.println(loc1);
            System.out.println(loc2);
            System.out.println(road1);
        } catch (IllegalArgumentException e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
}