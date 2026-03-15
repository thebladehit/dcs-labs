package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Utils {
    static public void printMatrix(double[][] M) {
        System.out.println("Result matrix:");
        for (double[] row : M) {
            for (double val : row) System.out.printf("%8.2f ", val);
            System.out.println();
        }
    }

    static public void printVector(double[] V) {
        System.out.println("Result vector:");
        for (double val : V) System.out.printf("%8.2f ", val);
    }

    public static void saveMatrixToFile(double[][] M, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (double[] row : M) {
                for (int j = 0; j < row.length; j++) {
                    writer.print(row[j]);
                    if (j < row.length - 1) writer.print(" ");
                }
                writer.println();
            }
            System.out.println("Matrix saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveVectorAsRow(double[] V, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (int i = 0; i < V.length; i++) {
                writer.print(V[i]);
                if (i < V.length - 1) writer.print(" ");
            }
            writer.println();
            System.out.println("Vector saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
