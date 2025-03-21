package pages;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BaseClass;

public class PetPage 
{
    WebDriver driver;
    WebDriverWait wait;

    // Locators
    private By searchBox = By.name("keyword");
    private By searchButton = By.name("searchProducts");
    private By petLink = By.xpath("//div[@id='Catalog']/table/tbody/tr/td/a"); 
    private By addToCartButton = By.xpath("//a[contains(text(),'Add to Cart')]");
    private By cartItem = By.xpath("//table/tbody/tr[2]/td/a");

    public PetPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // Search for a pet
    public void searchPet(String petName) 
    {
        try {
            WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
            searchField.clear();
            searchField.sendKeys(petName);
            driver.findElement(searchButton).click();
        } 
        catch (NoSuchElementException e) {
            System.out.println("Search box or button not found: " + e.getMessage());
        }
    }

    // Select the first pet from search results (Clicks on pet name or image)
    public void selectPet()
    {
        try {
            WebElement pet = wait.until(ExpectedConditions.elementToBeClickable(petLink));
            pet.click();
        } 
        catch (NoSuchElementException e)
        {
            System.out.println("Pet not found in search results: " + e.getMessage());
        }
    }

    // Add selected pet to the cart
    public void addToCart() 
    {
        try {
            WebElement addToCart = wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));
            addToCart.click();
        }
        catch (NoSuchElementException e)
        {
            System.out.println("Add to Cart button not found: " + e.getMessage());
        }
    }

    // Verify if pet is added to the cart by checking for the added item
    public boolean isPetAddedToCart() throws IOException 
    {
        try {
        	BaseClass.screenshot();
            return wait.until(ExpectedConditions.presenceOfElementLocated(cartItem)).isDisplayed();
        } 
        catch (NoSuchElementException e) 
        {
            System.out.println("Pet not found in cart: " + e.getMessage());
            return false;
        }
    }
}