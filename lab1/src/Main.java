//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
//        Data.generateAndSaveData();
        Data data = Data.readFromDisk();
        Local_ver.start(data);
        Global_ver.start(data);
    }
}