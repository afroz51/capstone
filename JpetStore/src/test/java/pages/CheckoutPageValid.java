package pages;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;

public class CheckoutPageValid {
    WebDriver driver;
    WebDriverWait wait;

    // **Constructor**
    public CheckoutPageValid(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // **Locators (Using @FindBy for POM)**
    @FindBy(linkText = "Sign In") private WebElement signInLink;
    @FindBy(name = "username") private WebElement usernameField;
    @FindBy(name = "password") private WebElement passwordField;
    @FindBy(name = "signon") private WebElement signOnButton;

    @FindBy(xpath = "//input[@name='keyword']") private WebElement search;
    @FindBy(xpath = "//input[@name='searchProducts']") private WebElement searchbutton;
    @FindBy(xpath = "//div[@id='Catalog']/table/tbody/tr/td/a") private WebElement pet;
    @FindBy(xpath = "//a[contains(text(),'Add to Cart')]") private WebElement addToCartButton;

    @FindBy(linkText = "Proceed to Checkout") private WebElement proceedToCheckoutButton;

    @FindBy(name = "order.billToFirstName") private WebElement billFirstName;
    @FindBy(name = "order.billToLastName") private WebElement billLastName;
    @FindBy(name = "order.billAddress1") private WebElement billAddress1;
    @FindBy(name = "order.billAddress2") private WebElement billAddress2;
    @FindBy(name = "order.billCity") private WebElement billCity;
    @FindBy(name = "order.billState") private WebElement billState;
    @FindBy(name = "order.billZip") private WebElement billZip;
    @FindBy(name = "order.billCountry") private WebElement billCountry;
    @FindBy(name = "order.cardType") private WebElement cardTypeDropdown;
    @FindBy(name = "order.creditCard") private WebElement cardNumberField;
    @FindBy(name = "order.expiryDate") private WebElement expiryDateField;

    @FindBy(xpath = "//input[@name='newOrder']") private WebElement submitOrderButton;
    @FindBy(xpath = "//div[@id='Catalog']/a[@class='Button']") private WebElement confirmorderbutton;

    @FindBy(xpath = "//ul[@class='messages']/li") private WebElement success; // Updated element

    // **Step 1: Sign in before checkout**
    @Step("Signing in before checkout")
    public void signIn(String userid, String password) {
        try {
            signInLink.click();
            clearAndSendKeys(usernameField, userid);
            clearAndSendKeys(passwordField, password);
            signOnButton.click();
            Allure.step("User attempted to sign in with invalid credentials.");
        } catch (NoSuchElementException e) {
            handleException("Sign-in elements not found!", e);
        }
    }

    // **Step 2: Add product to cart**
    @Step("Adding product to cart")
    public void addProductToCart(String item) {
        try {
            search.clear();
            search.sendKeys(item);
            searchbutton.click();
            pet.click();
            addToCartButton.click();
            Allure.step("Product added to cart.");
        } catch (NoSuchElementException e) {
            handleException("Product or 'Add to Cart' button not found!", e);
        }
    }

    // **Step 3: Proceed to Checkout**
    @Step("Proceeding to checkout")
    public void proceedToCheckout() {
        try {
            proceedToCheckoutButton.click();
            Allure.step("Navigated to Checkout page.");
        } catch (NoSuchElementException e) {
            handleException("Proceed to Checkout button not found!", e);
        }
    }

    // **Step 4: Fill Checkout Details**
    @Step("Filling checkout details")
    public void fillCheckoutDetails(String firstName, String lastName, String address1, String address2,
                                    String city, String state, String zipCode, String country,
                                    String cardType, String cardNumber, String expiryDate) throws IOException, InterruptedException {
        try {
            cardTypeDropdown.sendKeys(cardType);
            cardNumberField.sendKeys(cardNumber);
            clearAndSendKeys(expiryDateField, expiryDate);
            clearAndSendKeys(billFirstName, firstName);
            clearAndSendKeys(billLastName, lastName);
            clearAndSendKeys(billAddress1, address1);
            clearAndSendKeys(billAddress2, address2);
            clearAndSendKeys(billCity, city);
            clearAndSendKeys(billState, state);
            clearAndSendKeys(billZip, zipCode);
            clearAndSendKeys(billCountry, country);

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            Thread.sleep(3000);

            Allure.step("Checkout form filled with invalid data.");
        } catch (NoSuchElementException e) {
            handleException("Checkout form elements not found!", e);
        }
    }

    // **Step 5: Complete Checkout**
    @Step("Completing checkout process")
    public void completeCheckout() throws IOException {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(submitOrderButton)).click();
            wait.until(ExpectedConditions.elementToBeClickable(confirmorderbutton)).click();
            Allure.step("Clicked 'Submit Order'.");
        } catch (NoSuchElementException e) {
            handleException("Submit Order button not found!", e);
        }
    }

    // **Step 6: Verify if Checkout Error Message is Displayed**
    @Step("Checking for error message after invalid checkout")
    public boolean isCheckoutSuccessMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(success));
            Allure.step("Success message found: " + success.getText());
            return true; // Test should pass if the error message is found
        } catch (NoSuchElementException e) {
            Allure.step("Checkout error message not found!");
            return false;
        }
    }

    // **Method to clear text field before sending keys**
    private void clearAndSendKeys(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }

    // **Exception Handling Method**
    private void handleException(String message, NoSuchElementException e) {
        Allure.step("Error: " + message);
        throw new NoSuchElementException(message + " " + e.getMessage());
    }
}
