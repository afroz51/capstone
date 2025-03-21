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

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import excel.ExcelUtility;

public class BaseClass extends ExcelUtility
{
    public static WebDriver driver;
    public static Properties prop;
    public static ExtentReports extent;
    public static ExtentTest test;

    // Initialize Extent Reports
    public static void initExtentReport()
    {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportPath = System.getProperty("user.dir") + "/target/ExtentReports/ExtentReport_" + timestamp + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
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
        String screenshotPath = System.getProperty("user.dir") + "/screenshots/" + "page _" + System.currentTimeMillis() + ".png";
        FileUtils.copyFile(src, new File(screenshotPath));
        return screenshotPath; // Return full path of screenshot
    }



    // Close Browser & Flush Extent Report
    public static void closeBrowser()
    {
        if (driver != null) 
        {
            driver.quit();
        }
    }
    
    // It ensures that all logs and test execution details are written to the report.
    public static void flushExtentReport() 
    {
        if (extent != null) 
        {
            extent.flush(); // Save and close the Extent Report
        }
    }

}