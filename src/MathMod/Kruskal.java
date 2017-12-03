package MathMod;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Kruskal {

    private static final String INPUT_FILE_PATH = "input.txt";

    private static int vNum;

    public static void main(String[] args) {
        Edge[] edges = readMatrixFromFile();
        System.out.println(mstKruskal(edges));
    }

    private static Edge[] readMatrixFromFile() {
        List<Edge> edges = new ArrayList<>();

        try (Scanner scanner = new Scanner(new FileInputStream(INPUT_FILE_PATH))) {
            int matrixSize = Integer.parseInt(scanner.nextLine());
            vNum = matrixSize;
            for (int row = 0; row < matrixSize; row++) {
                String[] numbers = scanner.nextLine().split(" ");
                for (int col = row; col < matrixSize; col++) {
                    if (!numbers[col].equals("INF")) {
                        edges.add(new Edge(row, col, Integer.parseInt(numbers[col])));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return edges.toArray(new Edge[]{});
    }

    private static int mstKruskal(Edge[] edges) {
        DSF dsf = new DSF(vNum);
        Arrays.sort(edges);
        int ret = 0;
        for (Edge e : edges)
            if (dsf.union(e.u, e.v)) {
                ret += e.w;
                System.out.println((e.u + 1) + " -> " + (e.v + 1));
            }
        return ret;
    }

    static class Edge implements Comparable<Edge> {

        int u;

        int v;

        int w;

        Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }

        @Override
        public int compareTo(Edge edge) {
            if (w != edge.w) {
                return w < edge.w ? -1 : 1;
            }
            return 0;
        }

        @Override
        public String toString() {
            return String.format("u = %d   v = %d   w = %d\n", u + 1, v + 1, w);
        }

    }

    static class DSF {

        int[] set;

        int[] rnk;

        DSF(int size) {
            set = new int [size];
            rnk = new int [size];

            for (int i = 0; i < size; i++) {
                set[i] = i;
            }
        }

        int set(int x) {
            return x == set[x] ? x : (set[x] = set(set[x]));
        }

        boolean union(int u, int v) {
            if ((u = set(u)) == (v = set(v))) {
                return false;
            }

            if (rnk[u] < rnk[v]) {
                set[u] = v;
            } else {
                set[v] = u;
                if (rnk[u] == rnk[v]) {
                    rnk[u]++;
                }
            }

            return true;
        }
    }

}
