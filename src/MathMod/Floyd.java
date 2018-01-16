package MathMod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Floyd {

    private static final String INPUT_FILE_PATH = "input.txt";

    private static final String OUTPUT_FILE_PATH = "output.txt";

    private static final int INF = Integer.MAX_VALUE / 2;

    private static int begin;

    private static int end;

    public static void main(String[] args) {
        int[][] matrix = readMatrixFromFile();
        floyd(matrix);
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

            begin = scanner.nextInt();
            end = scanner.nextInt();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matrix;
    }

    private static void floyd(int[][] L) {
        int[][] S = new int[L.length][L.length];
        fillMatrix(S);

        for (int k = 0; k < L.length; k++) {
            for (int i = 0; i < L.length; i++) {
                for (int j = 0; j < L.length; j++) {
                    if (L[i][k] + L[k][j] < L[i][j]) {
                        L[i][j] = L[i][k] + L[k][j];
                        S[i][j] = S[i][k];
                    }
                }
            }
        }

        String path = findPath(L, S, begin, end);
        writeAnswerInFile(L, S, path);
    }

    private static void fillMatrix(int[][] S) {
        for (int i = 0; i < S.length; i++) {
            for (int j = 0; j < S.length; j++) {
                S[i][j] = j + 1;
            }
        }
    }

    private static String findPath(int[][] L, int[][] S, int begin, int end) {
        begin--;
        end--;

        String path;

        if (L[begin][end] == INF) {
            path = String.format("No path from %d to %d\n", begin + 1, end + 1);
        } else {
            path = String.format("Length of path from %d to %d is %d\n", begin + 1, end + 1, L[begin][end]);

            List<Integer> pathList = new LinkedList<>();
            pathList.add(begin + 1);

            while (S[begin][end] != end + 1) {
                begin = S[begin][end] - 1;
                pathList.add(begin + 1);
            }

            pathList.add(end + 1);

            path += pathList.stream().map(Object::toString).collect(Collectors.joining(" -> "));
        }

        return path;
    }

    private static void writeAnswerInFile(int[][] L, int[][] S, String path) {
        try (FileWriter fileWriter = new FileWriter(new File(OUTPUT_FILE_PATH))) {
            fileWriter.write("Matrix L:\n");

            for (int i = 0; i < L.length; i++) {
                for (int j = 0; j < L.length; j++) {
                    if (L[i][j] == INF) {
                        fileWriter.write("INF ");
                    } else {
                        fileWriter.write(L[i][j] + " ");
                    }
                }
                fileWriter.write("\n");
            }

            fileWriter.write("\nMatrix S:\n");
            for (int i = 0; i < S.length; i++) {
                for (int j = 0; j < S.length; j++) {
                    fileWriter.write(S[i][j] + " ");
                }
                fileWriter.write("\n");
            }

            fileWriter.write("\n" + path);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
