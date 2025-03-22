package base;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class AllureReportUtil {

    public static void generateAllureReport() {
        try {
            System.out.println("🟢 Generating Allure Report...");

            // Ensure test results are written before generating the report
            Thread.sleep(3000);

            // Define the report path
            String reportPath = System.getProperty("user.dir") + "/target/site/allure-maven-plugin/index.html";
            File reportFile = new File(reportPath);

            // Delete previous reports before generating a new one
            cleanPreviousAllureReports();

            // Run Allure Report command
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "mvn allure:report");
            builder.redirectErrorStream(true);
            Process process = builder.start();
            process.waitFor(); // Wait for process completion

            // Check if the report exists and open it
            if (reportFile.exists()) {
                System.out.println("✅ Allure Report generated successfully: " + reportPath);
                Desktop.getDesktop().browse(reportFile.toURI()); // Open report in browser
            } else {
                System.out.println("❌ Allure Report generation failed. Check Maven logs.");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Method to delete previous Allure reports before generating a new one
    private static void cleanPreviousAllureReports() {
        File allureDir = new File(System.getProperty("user.dir") + "/target/site/allure-maven-plugin");
        if (allureDir.exists()) {
            deleteDirectory(allureDir);
            System.out.println("🗑️ Deleted previous Allure reports.");
        }
    }

    // Recursive method to delete directories
    private static void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
}
