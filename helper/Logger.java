import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    private static ArrayList<String[]> timeStampPackages = new ArrayList<>();

    public static void generateLogCallMethod(String name, boolean start) {

        String timeStamp = String.valueOf(System.nanoTime());
        String locus = start ? "START" : "END";
        String[] timeStampPackage = new String[]{locus, name, timeStamp};
        timeStampPackages.add(timeStampPackage);

    }

    public static void writePackagesToFile() {
        try {
            String logPath = "./logs/timeLogs.txt";
            FileWriter fileWriter = new FileWriter(logPath, false);
            for (int i = 0; i < timeStampPackages.size(); i++) {
                String[] timeStampPackage = timeStampPackages.get(i);
                String timeStamp = timeStampPackage[0] + " " + timeStampPackage[1] + " " + timeStampPackage[2];
                fileWriter.write(timeStamp + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            // Nothing
        }
    }
}
