package pages;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BaseClass;

public class LoginPage extends BaseClass 
{
    WebDriver driver;
    WebDriverWait wait;

    public LoginPage(WebDriver driver) 
    {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Locators
    By signin = By.linkText("Sign In");
    By username = By.name("username");
    By password = By.name("password");
    By loginBtn = By.name("signon");
    By errorMsg = By.xpath("//ul[@class='messages']/li");

    // Method to Open Login Page
    public void openLoginPage()
    {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(signin)).click();
            test.pass("Opened Login Page");
        } 
        catch (NoSuchElementException e) 
        {
            test.fail("Sign In link not found: " + e.getMessage());
        }
    }

    // Method to Perform Login
    public void login(String user, String pass) 
    {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(username)).sendKeys(user);
            WebElement passField = wait.until(ExpectedConditions.visibilityOfElementLocated(password));
            passField.clear();
            passField.sendKeys(pass);
            test.pass("Entered credentials: " + user);
        } 
        catch (NoSuchElementException e) 
        {
            test.fail("Username or Password field not found: " + e.getMessage());
        }
    }

    // Method to Click Login Button
    public void submit() 
    {
        try 
        {
            wait.until(ExpectedConditions.elementToBeClickable(loginBtn)).click();
            test.pass("Clicked on Login button");
            captureScreenshot("LoginAttempt");
        } 
        catch (NoSuchElementException e) 
        {
            test.fail("Login button not found: " + e.getMessage());
        }
    }

    // Method to Check Error Message
    public boolean isErrorMessageDisplayed()
    {
        try 
        {
            boolean isDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMsg)).isDisplayed();
            test.pass("Error message displayed");
            return isDisplayed;
        } 
        catch (NoSuchElementException e) 
        {
            test.fail("Error message not found: " + e.getMessage());
            return false;
        }
    }

    // Method to Verify Login Success
    public boolean isLoginSuccessful()
    {
        boolean status = driver.getCurrentUrl().equals("https://petstore.octoperf.com/actions/Catalog.action");
        if (status) 
        {
            test.pass("Login successful, redirected to homepage");
        } 
        else 
        {
            test.fail("Login failed, incorrect URL");
        }
        return status;
    }

    // Method to Capture Screenshot
    public void captureScreenshot(String testName)
    {
        try {
            String screenshotPath = screenshot();
            test.addScreenCaptureFromPath(screenshotPath);
        } 
        catch (IOException e)
        {
            test.fail("Failed to capture screenshot: " + e.getMessage());
        }
    }
}
