public class Calculations {
    static double[][] multiplyMatrices(double[][] MA, double[][] MB, int start, int end) {
        double[][] res = new double[Data.N][Data.N];

        for (int i = start; i < end; i++) {
            for (int j = 0; j < Data.N; j++) {
                double sum = 0.0;
                double c = 0.0;
                for (int k = 0; k < Data.N; k++) {
                    double mult = MA[i][k] * MB[k][j];
                    double y = mult - c;
                    double t = sum + y;
                    c = (t - sum) - y;
                    sum = t;
                }
                res[i][j] = sum;
            }
        }

        return res;
    }

    static double[][] sumMatrices(double[][] MA, double[][] MB, int start, int end) {
        double[][] res = new double[Data.N][Data.N];

        for (int i = start; i < end; i++) {
            for (int j = 0; j < Data.N; j++) {
                res[i][j] = MA[i][j] + MB[i][j];
            }
        }

        return res;
    }

    static double[][] substructMatrices(double[][] MA, double[][] MB, int start, int end) {
        double[][] res = new double[Data.N][Data.N];

        for (int i = start; i < end; i++) {
            for (int j = 0; j < Data.N; j++) {
                res[i][j] = MA[i][j] - MB[i][j];
            }
        }

        return res;
    }

    static double findGlobalMaxFromVector(double[] A) {
        double max = 0;

        for (double v : A) {
            if (v > max) {
                max = v;
            }
        }

        return max;
    }

    static double[] multiplyMatrixOnVector(double[][] MA, double[] B, int start, int end) {
        double[] res = new double[Data.N];

        for (int i = start; i < end; i++) {
            double sum = 0.0;
            double c = 0.0;
            for (int j = 0; j < Data.N; j++) {
                double mult = MA[i][j] * B[j];
                double y = mult - c;
                double t = sum + y;
                c = (t - sum) - y;
                sum = t;
            }
            res[i] = sum;
        }

        return res;
    }

    static double[] multiplyScalarOnVector(double[] A, double B, int start, int end) {
        double[] res = new double[Data.N];

        for (int i = start; i < end; i++) {
            res[i] = B * A[i];
        }

        return res;
    }

    static double[] substructVectors(double[] A, double[] B, int start, int end) {
        double[] res = new double[Data.N];

        for (int i = start; i < end; i++) {
            res[i] = A[i] - B[i];
        }

        return res;
    }
}
