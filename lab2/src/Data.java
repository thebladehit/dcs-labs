import java.io.*;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Data implements Serializable {
    public static final String FILE_NAME = "data-100";
    public static final int N = 100;
    public static final int THREADS_NUMBER = 4;
    private static final long serialVersionUID = 8948642304816950726L;

    public double[][] MD, MT, MZ, ME, MM;
    public double[] D, B;

    public transient Lock lockMD = new ReentrantLock();
    public transient Lock lockMT = new ReentrantLock();
    public transient Lock lockMZ = new ReentrantLock();
    public transient Lock lockME = new ReentrantLock();
    public transient Lock lockMM = new ReentrantLock();
    public transient Lock lockD = new ReentrantLock();
    public transient Lock lockB = new ReentrantLock();

    public Data() {
        MD = new double[N][N];
        MT = new double[N][N];
        MZ = new double[N][N];
        ME = new double[N][N];
        MM = new double[N][N];
        D = new double[N];
        B = new double[N];
    }

    @Serial
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        lockMD = new ReentrantLock();
        lockMT = new ReentrantLock();
        lockMZ = new ReentrantLock();
        lockME = new ReentrantLock();
        lockMM = new ReentrantLock();
        lockD = new ReentrantLock();
        lockB = new ReentrantLock();
    }

    public static void generateAndSaveData() {
        Data data = new Data();
        Random random = new Random();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                data.MD[i][j] = getRandomValue(random);
                data.MT[i][j] = getRandomValue(random);
                data.MZ[i][j] = getRandomValue(random);
                data.ME[i][j] = getRandomValue(random);
                data.MM[i][j] = getRandomValue(random);
            }
            data.D[i] = getRandomValue(random);
            data.B[i] = getRandomValue(random);
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(FILE_NAME)))) {
            out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Data readFromDisk() {
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(FILE_NAME)))) {
            return (Data) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static double getRandomValue(Random random) {
        return Math.pow(10, -4 + 10 * random.nextDouble());
    }
}