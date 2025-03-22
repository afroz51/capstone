package pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.function.Function;

public class CartPage
{
    WebDriver driver;
    WebDriverWait wait;

    // Locators for Cart Page
    private By searchBox = By.name("keyword");
    private By searchButton = By.name("searchProducts");
    private By petLink = By.xpath("//div[@id='Catalog']/table/tbody/tr/td/a"); 
    private By addToCartButton = By.xpath("//a[contains(text(),'Add to Cart')]");
    private By cartItem = By.xpath("//table/tbody/tr[2]/td/a");
    
    // Different paths for each product added to cart
    private By cartItem2 = By.xpath("//table/tbody/tr[3]/td/a");
    private By cartItem3 = By.xpath("//table/tbody/tr[2]/td/a");
    private By cartItem4 = By.xpath("//table/tbody/tr[4]/td/a");

    public CartPage(WebDriver driver) 
    {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // Search for a product
    public void searchProduct(String[] searchItems, int index) 
    {
        try {
            if (index < 0 || index >= searchItems.length) {
                throw new IllegalArgumentException("Invalid index for search items array.");
            }

            String productName = searchItems[index].trim(); // Get product by index

            WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
            searchField.clear();
            searchField.sendKeys(productName);
            driver.findElement(searchButton).click();
        } 
        catch (NoSuchElementException e) 
        {
            System.out.println("Search box or button not found: " + e.getMessage());
        }
    }

    // Select the product link from search results (Using FluentWait)
    public void selectProduct() 
    {
        try {
            FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);

            WebElement product = fluentWait.until(new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    return driver.findElement(petLink);
                }
            });

            product.click();
        } 
        catch (NoSuchElementException e) 
        {
            System.out.println("Product not found in search results: " + e.getMessage());
        }
    }

    // Add product to the cart (Using FluentWait)
    public void addProductToCart()
    {
        try {
            FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);

            WebElement addToCart = fluentWait.until(new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    return driver.findElement(addToCartButton);
                }
            });

            addToCart.click();
        }
        catch (NoSuchElementException e)
        {
            System.out.println("Add to Cart button not found: " + e.getMessage());
        }
    }

    // Verify if product was successfully added to the cart (Checking based on the product's link)
    public boolean isProductAddedToCart(String productNumber) 
    {
        try {
            WebElement cartItemElement = null;
            switch (productNumber) {
                case "1":
                    cartItemElement = wait.until(ExpectedConditions.presenceOfElementLocated(cartItem));
                    break;
                case "2":
                    cartItemElement = wait.until(ExpectedConditions.presenceOfElementLocated(cartItem2));
                    break;
                case "3":
                    cartItemElement = wait.until(ExpectedConditions.presenceOfElementLocated(cartItem3));
                    break;
                case "4":
                    cartItemElement = wait.until(ExpectedConditions.presenceOfElementLocated(cartItem4));
                    break;
            }
            return cartItemElement.isDisplayed();
        } 
        catch (NoSuchElementException e)
        {
            System.out.println("Product not found in cart: " + e.getMessage());
            return false;
        }
    }

    // Clear the search box after each product is added
    public void clearSearchBox() 
    {
        WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
        searchField.clear();
    }
}
