package base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import excel.ExcelUtility;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;

public class BaseClass extends ExcelUtility
{
    public static WebDriver driver;
    public static Properties prop;
    public static ExtentReports extent;
    public static ExtentTest test;
    private static String extentReportPath;

    // Initialize Extent Reports
    public static void initExtentReport()
    {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        extentReportPath = System.getProperty("user.dir") + "/target/ExtentReports/ExtentReport_" + timestamp + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(extentReportPath);
        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Browser", "Chrome"); // Default value, update dynamically
    }

    // Initialize WebDriver
    public static void invokeBrowser()
    {
        if (extent == null) {
            initExtentReport();  // Ensure Extent Report is initialized
        }

        loadProperties();
        loadExcelData();
        String browser = prop.getProperty("browser");

        switch (browser.toLowerCase()) 
        {
            case "chrome":
                driver = new ChromeDriver();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            default:
                throw new RuntimeException("Invalid browser name in data.properties");
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(prop.getProperty("url"));
    }
    
    public static void invokeBrowser(String browser, String url)
    {
        if (extent == null) 
        {
            initExtentReport(); // Ensure Extent Report is initialized
        }

        loadProperties();
        loadExcelData();
        
        switch (browser.toLowerCase()) 
        {
            case "chrome":
                driver = new ChromeDriver();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            default:
                throw new RuntimeException("Invalid browser name: " + browser);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(url);
    }

    // Load data from properties file
    public static void loadProperties()
    {
        prop = new Properties();
        try (FileInputStream ip = new FileInputStream("src/test/java/data/data.properties")) {
            prop.load(ip);
        } 
        catch (IOException e) 
        {
            throw new RuntimeException("Failed to load properties file", e);
        }
    }

    // Load data from Excel file
    public static void loadExcelData()
    {
        try {
            ExcelUtility.loadExcelData("C:\\Users\\AFROZ\\Downloads\\data.xlsx");
            setSheet("userdata");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    // Capture Screenshot and return file path
    public static String screenshot() throws IOException 
    {
        new WebDriverWait(driver, Duration.ofSeconds(3)) // Wait for UI to stabilize before taking screenshot
            .until(d -> ((TakesScreenshot) d).getScreenshotAs(OutputType.FILE) != null);

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String screenshotPath = System.getProperty("user.dir") + "/screenshots/" + "page_" + System.currentTimeMillis() + ".png";
        FileUtils.copyFile(src, new File(screenshotPath));
        
        attachScreenshotAllure(); // Attach to Allure Report
        return screenshotPath; // Return full path of screenshot
    }

    // Attach Screenshot to Allure Report
    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] attachScreenshotAllure() 
    {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } 
        catch (Exception e) 
        {
            return null;
        }
    }

    // Log message to Allure Report
    public static void logToAllure(String message)
    {
        Allure.step(message);
    }

    // Close Browser & Flush Reports
    public static void closeBrowser()
    {
        if (driver != null) 
        {
            driver.quit();
        }
    }
    
    // Flush Extent Report
    public static void flushExtentReport() 
    {
        if (extent != null) 
        {
            extent.flush(); // Save and close the Extent Report
        }
    }

    // Generate Allure Report after test execution
    @AfterSuite
    public void generateAllureReport() 
    {
        flushExtentReport(); // Ensure Extent Reports are saved first
        
        try 
        {
            // Move Allure results to target/site/allure-maven-plugin/
            File allureResults = new File(System.getProperty("user.dir") + "/allure-results");
            File allureReportDir = new File(System.getProperty("user.dir") + "/target/site/allure-maven-plugin");
            
            if (!allureReportDir.exists()) {
                allureReportDir.mkdirs();
            }

            FileUtils.copyDirectory(allureResults, allureReportDir);

            // Generate Allure Report (ensure proper command execution)
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "allure serve target/site/allure-maven-plugin");
            pb.inheritIO();
            Process process = pb.start();
            process.waitFor(); // Wait for the process to complete before exiting
        } 
        catch (IOException | InterruptedException e) 
        {
            e.printStackTrace();
        }
    }
}
