package pages;

import base.BaseClass;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;

public class RegistrationPage extends BaseClass 
{
    WebDriver driver;
    WebDriverWait wait;

    public RegistrationPage(WebDriver driver) 
    {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Locators
    By signin = By.linkText("Sign In");
    By registerBtn = By.linkText("Register Now!");
    By submitBtn = By.name("newAccount");
    By errorMsg = By.xpath("//h1"); 

    // User details locators
    By userid = By.name("username");
    By newpassword = By.name("password");
    By repeatpassword = By.name("repeatedPassword");
    By firstname = By.name("account.firstName");
    By lastname = By.name("account.lastName");
    By email = By.name("account.email");
    By phone = By.name("account.phone");
    By address1 = By.name("account.address1");
    By address2 = By.name("account.address2");
    By city = By.name("account.city");
    By state = By.name("account.state");
    By zip = By.name("account.zip");
    By country = By.name("account.country");
    By language = By.name("account.languagePreference");
    By favoriteCategory = By.name("account.favouriteCategoryId");
    By enableMyList = By.name("account.listOption");
    By enableMyBanner = By.name("account.bannerOption");

    // Method to Open Registration Page
    public void openRegistrationPage() 
    {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(signin)).click();
            wait.until(ExpectedConditions.elementToBeClickable(registerBtn)).click();
            test.pass("Navigated to Registration Page");
        } 
        catch (NoSuchElementException e)
        {
            test.fail("Error opening Registration Page: " + e.getMessage());
        }
    }

    // Method to Fill Registration Form
    public void fillRegistrationForm(String id, String np, String rp, String fn, String ln, String em, String ph,
                                     String add1, String add2, String ci, String st, String zp, String co,
                                     String lang, String favCat, boolean listOption, boolean bannerOption) 
    {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(userid)).sendKeys(id);
            driver.findElement(newpassword).sendKeys(np);
            driver.findElement(repeatpassword).sendKeys(rp);
            driver.findElement(firstname).sendKeys(fn);
            driver.findElement(lastname).sendKeys(ln);
            driver.findElement(email).sendKeys(em);
            driver.findElement(phone).sendKeys(ph);
            driver.findElement(address1).sendKeys(add1);
            driver.findElement(address2).sendKeys(add2);
            driver.findElement(city).sendKeys(ci);
            driver.findElement(state).sendKeys(st);
            driver.findElement(zip).sendKeys(zp);
            driver.findElement(country).sendKeys(co);
            driver.findElement(language).sendKeys(lang);
            driver.findElement(favoriteCategory).sendKeys(favCat);

            if (listOption) 
            {
                driver.findElement(enableMyList).click();
            }
            if (bannerOption) 
            {
                driver.findElement(enableMyBanner).click();
            }
            test.pass("Filled Registration Form successfully");
        } 
        catch (NoSuchElementException e) 
        {
            test.fail("Error filling Registration Form: " + e.getMessage());
        }
    }

    // Method to Submit Registration Form
    public void submitForm() 
    {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(submitBtn)).click();
            captureScreenshot("RegistrationAttempt");
            test.pass("Submitted Registration Form");
        } 
        catch (NoSuchElementException e)
        {
            test.fail("Error submitting form: " + e.getMessage());
        }
    }

    // Method to Check Error Message
    public boolean isErrorMessageDisplayed()
    {
        try {
            boolean isDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMsg)).isDisplayed();
            test.pass("Error message displayed");
            return isDisplayed;
        } 
        catch (NoSuchElementException e)
        {
            test.fail("No error message displayed: " + e.getMessage());
            return false;
        }
    }

    // Method to Verify Registration Success
    public boolean isRegistrationSuccessful() 
    {
        boolean status = driver.getCurrentUrl().equals("https://petstore.octoperf.com/actions/Catalog.action");
        if (status) 
        {
            test.pass("Registration successful, redirected to homepage");
        } 
        else 
        {
            test.fail("Registration failed, incorrect URL");
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
