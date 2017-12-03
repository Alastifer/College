package MathMod;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Prima {

    private static final String INPUT_FILE_PATH = "input.txt";

    private static final int INF = Integer.MAX_VALUE / 2;

    public static void main(String[] args) {
        int[][] matrix = readMatrixFromFile();
        mstPrim(matrix);
    }

    private static int[][] readMatrixFromFile() {
        int[][] matrix = null;

        try (Scanner scanner = new Scanner(new FileInputStream(INPUT_FILE_PATH))) {
            int matrixSize = Integer.parseInt(scanner.nextLine());
            matrix = new int[matrixSize][matrixSize];

            for (int row = 0; row < matrixSize; row++) {
                String[] numbers = scanner.nextLine().split(" ");
                for (int col = 0; col < matrixSize; col++) {
                    if (numbers[col].equals("INF")) {
                        matrix[row][col] = INF;
                    } else {
                        matrix[row][col] = Integer.parseInt(numbers[col]);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return matrix;
    }

    private static void mstPrim(int[][] graph) {
        int vNum = graph[0].length;
        boolean[] used = new boolean [vNum];
        int[] dist = new int [vNum];
        int[] way = new int[vNum];

        Arrays.fill(dist, INF);
        Arrays.fill(way, -1);
        dist[0] = 0;

        while (true) {
            int v = -1;
            for (int nv = 0; nv < vNum; nv++) {
                if (!used[nv] && dist[nv] < INF && (v == -1 || dist[v] > dist[nv]))
                    v = nv;
            }

            if (v == -1) {
                break;
            }

            used[v] = true;
            for (int nv = 0; nv < vNum; nv++) {
                if (!used[nv] && Math.min(dist[nv], graph[v][nv]) == graph[v][nv]) {
                    dist[nv] = graph[v][nv];
                    way[nv] = v;
                }
            }
        }

        int weight = 0;
        for (int v = 0; v < vNum; v++) {
            weight += dist[v];
        }

        showWay(way);
        System.out.println("Weight: " + weight);
    }

    private static void showWay(int[] way) {
        for (int i = 0; i < way.length; i++) {
            if (way[i] != -1) {
                System.out.println((way[i] + 1) + " -> " + (i + 1));
            }
        }
    }


}
