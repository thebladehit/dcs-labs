import utils.Utils;

import java.util.Arrays;

public class Local_ver  {
    static class Computing implements Runnable {
        private final int start, end, threadNumber;
        private final double[][] lMD, lMT, lMZ, lME, lMM;
        private double[][] lMA;
        private final double [] lD, lB;
        private double[] lA;

        Computing(int start, int end, Data data, int threadNumber) {
            this.start = start;
            this.end = end;
            this.threadNumber = threadNumber;

            lD = Arrays.copyOf(data.D, data.D.length);
            lB = Arrays.copyOf(data.B, data.B.length);

            lMD = new double[Data.N][Data.N];
            lMT = new double[Data.N][Data.N];
            lMZ = new double[Data.N][Data.N];
            lME = new double[Data.N][Data.N];
            lMM = new double[Data.N][Data.N];

            lMA = new double[Data.N][Data.N];
            lA = new double[Data.N];


            for (int i = 0; i < Data.N; i++) {
                lMD[i] = Arrays.copyOf(data.MD[i], data.MD[i].length);
                lMT[i] = Arrays.copyOf(data.MT[i], data.MT[i].length);
                lMZ[i] = Arrays.copyOf(data.MZ[i], data.MZ[i].length);
                lME[i] = Arrays.copyOf(data.ME[i], data.ME[i].length);
                lMM[i] = Arrays.copyOf(data.MM[i], data.MM[i].length);
            }
        }

        public double[] getlA() {
            return lA;
        }

        public double[][] getlMA() {
            return lMA;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        @Override
        public void run() {
            System.out.printf("Local version: thread %d started \n", threadNumber);

            lMA = this.calcMA();
            lA = this.calcA();

            System.out.printf("Local version: thread %d finished \n", threadNumber);
        }

        private double[][] calcMA() {
            // MD * MT
            double[][] MDMT = Calculations.multiplyMatrices(lMD, lMT, start, end);

            // MDMT + MZ
            double[][] MDMTMZ = Calculations.sumMatrices(MDMT, lMZ, start, end);

            // ME * MM
            double[][] MEMM = Calculations.multiplyMatrices(lME, lMM, start, end);

            // MDMTMZ - MEMM
            return Calculations.substructMatrices(MDMTMZ, MEMM, start, end);
        }

        private double[] calcA() {
            // MT * D
            double[] MTD = Calculations.multiplyMatrixOnVector(lMT, lD, start, end);

            // max(D)
            double maxD = Calculations.findGlobalMaxFromVector(lD);

            // maxD * B
            double[] maxDB = Calculations.multiplyScalarOnVector(lB, maxD, start, end);

            // MTD - maxDB
            return Calculations.substructVectors(MTD, maxDB, start, end);
        }
    }

    static public void start(Data data) throws InterruptedException {
        System.out.printf("Local Version: number of threads: %d\n", Data.THREADS_NUMBER);

        long startTime = System.currentTimeMillis();

        double[][] resMA = new double[Data.N][Data.N];
        double[] resA = new double[Data.N];

        Computing[] workers = new Computing[Data.THREADS_NUMBER];
        Thread[] threads = new Thread[Data.THREADS_NUMBER];

        int countPerThread = Data.N / Data.THREADS_NUMBER;
        for (int i = 0; i < Data.THREADS_NUMBER; i++) {
            int start = i * countPerThread;
            int end = start + countPerThread;

            workers[i] = new Computing(start, end, data, i);
            threads[i] = new Thread(workers[i]);
            threads[i].start();
        }

        for (Thread t : threads) t.join();

        for (Computing worker : workers) {
            int start = worker.getStart();
            int end = worker.getEnd();

            double[][] partMA = worker.getlMA();
            for (int i = start; i < end; i++) {
                System.arraycopy(partMA[i], 0, resMA[i], 0, Data.N);
            }

            double[] partA = worker.getlA();
            System.arraycopy(partA, start, resA, start, end - start);
        }


        long endTime = System.currentTimeMillis();
        System.out.printf("Duration of execution Local version: %20d\n", endTime - startTime);

        Utils.saveMatrixToFile(resMA, "local-MA.txt");
        Utils.saveVectorAsRow(resA, "local-A.txt");

        if (Data.N < 10) {
            Utils.printMatrix(resMA);
            Utils.printVector(resA);
        }
    }
}
