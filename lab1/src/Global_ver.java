import utils.Utils;

public class Global_ver {
    static class Computing implements Runnable {
        private final int start, end, threadNumber;
        private double[][] lMA;
        private double[] lA;
        private final Data data;

        Computing(int start, int end, Data data, int threadNumber) {
            this.start = start;
            this.end = end;
            this.data = data;
            this.threadNumber = threadNumber;
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
            System.out.printf("Global version: thread %d started \n", threadNumber);

            lMA = this.calcMA();
            lA = this.calcA();

            System.out.printf("Global version: thread %d finished \n", threadNumber);
        }

        private double[][] calcMA() {
            // MD * MT
            double[][] MDMT = Calculations.multiplyMatrices(data.MD, data.MT, start, end);

            // MDMT + MZ
            double[][] MDMTMZ = Calculations.sumMatrices(MDMT, data.MZ, start, end);

            // ME * MM
            double[][] MEMM = Calculations.multiplyMatrices(data.ME, data.MM, start, end);

            // MDMTMZ - MEMM
            return Calculations.substructMatrices(MDMTMZ, MEMM, start, end);
        }

        private double[] calcA() {
            // MT * D
            double[] MTD = Calculations.multiplyMatrixOnVector(data.MT, data.D, start, end);

            // max(D)
            double maxD = Calculations.findGlobalMaxFromVector(data.D);

            // maxD * B
            double[] maxDB = Calculations.multiplyScalarOnVector(data.B, maxD, start, end);

            // MTD - maxDB
            return Calculations.substructVectors(MTD, maxDB, start, end);
        }
    }

    static public void start(Data data) throws InterruptedException {
        System.out.printf("Global Version: number of threads: %d\n", Data.THREADS_NUMBER);

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
        System.out.printf("Duration of execution Global version: %20d\n", endTime - startTime);

        Utils.saveMatrixToFile(resMA, "global-MA.txt");
        Utils.saveVectorAsRow(resA, "global-A.txt");

        if (Data.N < 10) {
            Utils.printMatrix(resMA);
            Utils.printVector(resA);
        }
    }
}
