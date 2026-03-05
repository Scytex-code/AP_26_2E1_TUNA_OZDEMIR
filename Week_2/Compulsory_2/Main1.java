public class Main1 {
    public static void main(String[] args) {
        Location1 loc1 = new Location1("Istanbul", "City", 0, 0);
        Location1 loc2 = new Location1("Ankara", "City", 400, 300);

        try {
            Road1 road1 = new Road1("Highway", 550.0, 120, loc1, loc2);
            System.out.println(loc1);
            System.out.println(loc2);
            System.out.println(road1);
        } catch (IllegalArgumentException e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
}