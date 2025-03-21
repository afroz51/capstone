package tests;

import java.io.IOException;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import base.BaseClass;
import pages.CartPage;

public class CartTest extends BaseClass
{
    CartPage cartPage;

    @BeforeMethod
    @Parameters({"browser", "url"})
    public void setUp(String browser, String url)
    {
        invokeBrowser(browser, url);
        cartPage = new CartPage(driver);
    }

    // Data Provider: Fetch from Properties File
    @DataProvider(name = "cartDataFromProperties")
    public Object[][] getCartDataFromProperties() 
    {
        if (prop == null) 
        {
            loadProperties(); // Ensure properties file is loaded
        }

        // Fetch product names from properties file
        String product1 = prop.getProperty("product1");
        String product2 = prop.getProperty("product2");
        String product3 = prop.getProperty("product3");
        String product4 = prop.getProperty("product4");

        if (product1 == null || product2 == null || product3 == null || product4 == null) 
        {
            throw new RuntimeException("One or more product names are missing in data.properties.");
        }

        return new Object[][]{{product1, product2, product3, product4}};
    }

    // Data Provider: Fetch from Excel File 
    @DataProvider(name = "cartDataFromExcel")
    public Object[][] getCartDataFromExcel() 
    {
        // Ensure Excel data is loaded
        if (getWorkbook() == null) {
            loadExcelData("C:\\Users\\AFROZ\\Downloads\\data.xlsx");
        }

        // Ensure the sheet is set
        if (getSheet() == null) {
            setSheet("userdata");
        }

        // Fetch product names from Excel 
        String product1 = getCellData(1, 17);
        String product2 = getCellData(1, 18);
        String product3 = getCellData(1, 19);
        String product4 = getCellData(1, 20);

        // Validate data
        if (product1 == null || product2 == null || product3 == null || product4 == null) 
        {
            throw new RuntimeException("One or more product names are missing in Excel.");
        }

        return new Object[][]{{product1, product2, product3, product4}};
    }

    // Test 1: Fetch data from Properties File
    @Test(dataProvider = "cartDataFromProperties", enabled = false)
    public void testAddMultipleProductsToCart_PropertiesFile(String product1, String product2, String product3, String product4) throws IOException 
    {
        executeAddToCartTest(product1, product2, product3, product4, "Properties File");
    }

    // Test 2: Fetch data from Excel File
    @Test(dataProvider = "cartDataFromExcel", enabled = true)
    public void testAddMultipleProductsToCart_ExcelFile(String product1, String product2, String product3, String product4) throws IOException 
    {
        executeAddToCartTest(product1, product2, product3, product4, "Excel File");
    }

    // Reusable Test Execution Method
    private void executeAddToCartTest(String product1, String product2, String product3, String product4, String dataSource) throws IOException
    {
        try {
            test = extent.createTest("Adding Multiple Products to Cart (" + dataSource + "): " + product1 + ", " + product2 + ", " + product3 + ", " + product4);

            // Search and Add Product 1 to Cart
            cartPage.searchProduct(product1);
            cartPage.selectProduct();
            cartPage.addProductToCart();
            Assert.assertTrue(cartPage.isProductAddedToCart("1"), "Product 1 was not added to the cart!");

            // Search and Add Product 2 to Cart
            cartPage.clearSearchBox();
            cartPage.searchProduct(product2);
            cartPage.selectProduct();
            cartPage.addProductToCart();
            Assert.assertTrue(cartPage.isProductAddedToCart("2"), "Product 2 was not added to the cart!");

            // Search and Add Product 3 to Cart
            cartPage.clearSearchBox();
            cartPage.searchProduct(product3);
            cartPage.selectProduct();
            cartPage.addProductToCart();
            Assert.assertTrue(cartPage.isProductAddedToCart("3"), "Product 3 was not added to the cart!");

            // Search and Add Product 4 to Cart
            cartPage.clearSearchBox();
            cartPage.searchProduct(product4);
            cartPage.selectProduct();
            cartPage.addProductToCart();
            Assert.assertTrue(cartPage.isProductAddedToCart("4"), "Product 4 was not added to the cart!");
            
            // Take ScreenShot
            screenshot();

            // Verify Cart Summary (this could be a new method for cart validation)
            // Assuming a method to verify cart contents or total price, you can implement as needed
            test.pass("All products added to the cart successfully.");
        } 
        catch (NoSuchElementException e) 
        {
            handleTestFailure(e);
        }
    }

    // Reusable Failure Handling Method
    private void handleTestFailure(NoSuchElementException e)
    {
        test.fail("Test failed due to missing element: " + e.getMessage());
        try {
            String screenshotPath = screenshot();
            test.addScreenCaptureFromPath(screenshotPath);
        } 
        catch (IOException ioException)
        {
            test.fail("Failed to capture screenshot: " + ioException.getMessage());
        }
        Assert.fail("Test failed due to missing element.");
    }

    @AfterMethod
    public void close() 
    {
        closeBrowser();
        flushExtentReport();
    }
}
