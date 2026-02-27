package Week_1.Homework_Advanced_1;

import java.util.Arrays;

public class Matrix2D {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Matrix2D <size_n> <shape: rectangle|circle>");
            return;
        }

        int n = Integer.parseInt(args[0]);
        String shape = args[1].toLowerCase();

        long startTime = System.nanoTime();

        try {
            int[][] matrix = new int[n][n];
            int shapeColor;

            if (shape.equals("rectangle")) {
                generateRectangle(matrix, n);
                shapeColor = 0; 
            } else if (shape.equals("circle")) {
                generateCircle(matrix, n);
                shapeColor = 255;
            } else {
                System.out.println("Unknown shape. Use 'rectangle' or 'circle'.");
                return;
            }

            int[] box = findBoundingBox(matrix, n, shapeColor);
            
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1_000_000;

            if (n <= 50) {
                System.out.println(prettyPrint(matrix));
            }
            
            if (box != null) {
                System.out.println("--- Analysis ---");
                System.out.println("Bounding Box [minRow, minCol, maxRow, maxCol]: " + Arrays.toString(box));
                System.out.println("Dimensions: " + (box[2] - box[0] + 1) + "x" + (box[3] - box[1] + 1));
            }
            System.out.println("Execution time: " + duration + " ms");

        } catch (OutOfMemoryError e) {
            System.err.println("OutOfMemoryError! n=" + n + " is too large.");
        }
    }

    private static int[] findBoundingBox(int[][] matrix, int n, int targetColor) {
        int minRow = n, minCol = n, maxRow = -1, maxCol = -1;
        boolean found = false;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == targetColor) {
                    if (i < minRow) minRow = i;
                    if (i > maxRow) maxRow = i;
                    if (j < minCol) minCol = j;
                    if (j > maxCol) maxCol = j;
                    found = true;
                }
            }
        }
        return found ? new int[]{minRow, minCol, maxRow, maxCol} : null;
    }

    private static void generateRectangle(int[][] matrix, int n) {
        for (int i = 0; i < n; i++) Arrays.fill(matrix[i], 255);
        int margin = n / 4;
        for (int i = margin; i < n - margin; i++) {
            for (int j = margin; j < n - margin; j++) {
                matrix[i][j] = 0;
            }
        }
    }

    private static void generateCircle(int[][] matrix, int n) {
        int centerX = n / 2;
        int centerY = n / 2;
        int radius = n / 3;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double dist = Math.pow(i - centerX, 2) + Math.pow(j - centerY, 2);
                if (dist <= Math.pow(radius, 2)) {
                    matrix[i][j] = 255;
                }
            }
        }
    }

    public static String prettyPrint(int[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : matrix) {
            for (int cell : row) {
                sb.append(cell > 128 ? "░░" : "██");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}