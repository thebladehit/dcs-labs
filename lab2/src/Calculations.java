import java.util.concurrent.locks.Lock;

public class Calculations {

    static double[][] multiplyMatrices(double[][] MA, double[][] MB,
                                       Lock lockA, Lock lockB,
                                       int start, int end) {
        double[][] res = new double[Data.N][Data.N];
        for (int i = start; i < end; i++) {

            lockA.lock();
            lockB.lock();
            try {
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
            } finally {
                lockA.unlock();
                lockB.unlock();
            }

        }
        return res;
    }

    static double[][] sumMatrices(double[][] MA, double[][] MB,
                                  Lock lockA, Lock lockB,
                                  int start, int end) {
        double[][] res = new double[Data.N][Data.N];
        for (int i = start; i < end; i++) {

            lockA.lock();
            lockB.lock();
            try {
                for (int j = 0; j < Data.N; j++) {
                    res[i][j] = MA[i][j] + MB[i][j];
                }
            } finally {
                lockA.unlock();
                lockB.unlock();
            }

        }
        return res;
    }

    static double[][] substructMatrices(double[][] MA, double[][] MB,
                                        Lock lockA, Lock lockB,
                                        int start, int end) {
        double[][] res = new double[Data.N][Data.N];
        for (int i = start; i < end; i++) {

            lockA.lock();
            lockB.lock();
            try {
                for (int j = 0; j < Data.N; j++) {
                    res[i][j] = MA[i][j] - MB[i][j];
                }
            } finally {
                lockA.unlock();
                lockB.unlock();
            }

        }
        return res;
    }

    static double findGlobalMaxFromVector(double[] A, Lock lockA) {
        double max = 0;

        lockA.lock();
        try {
            for (double v : A) {
                if (v > max) max = v;
            }
        } finally {
            lockA.unlock();
        }

        return max;
    }

    static double[] multiplyMatrixOnVector(double[][] MA, double[] B,
                                           Lock lockMA, Lock lockB,
                                           int start, int end) {
        double[] res = new double[Data.N];
        for (int i = start; i < end; i++) {

            lockMA.lock();
            lockB.lock();
            try {
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
            } finally {
                lockMA.unlock();
                lockB.unlock();
            }

        }
        return res;
    }

    static double[] multiplyScalarOnVector(double[] A, double scalar,
                                           Lock lockA,
                                           int start, int end) {
        double[] res = new double[Data.N];
        for (int i = start; i < end; i++) {

            lockA.lock();
            try {
                res[i] = scalar * A[i];
            } finally {
                lockA.unlock();
            }

        }
        return res;
    }

    static double[] substructVectors(double[] A, double[] B,
                                     Lock lockA, Lock lockB,
                                     int start, int end) {
        double[] res = new double[Data.N];
        for (int i = start; i < end; i++) {

            lockA.lock();
            lockB.lock();
            try {
                res[i] = A[i] - B[i];
            } finally {
                lockA.unlock();
                lockB.unlock();
            }

        }
        return res;
    }
}