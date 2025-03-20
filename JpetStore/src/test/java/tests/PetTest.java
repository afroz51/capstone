package tests;

import java.io.IOException;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import base.BaseClass;
import pages.PetPage;

public class PetTest extends BaseClass 
{
    PetPage petPage;

    @BeforeMethod
    @Parameters({"browser", "url"})
    public void setUp(String browser, String url) 
    {
        invokeBrowser(browser, url);
        petPage = new PetPage(driver);
    }

    // Test 1: Fetch data from Excel 
    @Test(enabled = false)
    public void testSearchAndAddToCart_ExcelData() throws IOException 
    {
        String petName = getCellData(1, 17); 

        try {
            test = extent.createTest("Searching and Adding Pet (Excel Data): " + petName);

            petPage.searchPet(petName);
            petPage.selectPet();
            petPage.addToCart();
            
            Assert.assertTrue(petPage.isPetAddedToCart(), "Pet was not added to the cart!");
            test.pass("Pet added to cart successfully.");

        } 
        catch (NoSuchElementException e) 
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
    }

    // Test 2: Fetch data from properties file
    @Test(enabled = true)
    public void testSearchAndAddToCart_PropertiesFile() throws IOException
    {
        String petName = prop.getProperty("searchProduct"); // Fetch from data.properties

        try {
            test = extent.createTest("Searching and Adding Pet (Properties File): " + petName);

            petPage.searchPet(petName);
            petPage.selectPet();
            petPage.addToCart();
            
            Assert.assertTrue(petPage.isPetAddedToCart(), "Pet was not added to the cart!");
            test.pass("Pet added to cart successfully.");

        } 
        catch (NoSuchElementException e)
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
    }

    @AfterMethod
    public void close() 
    {
        closeBrowser();
        flushExtentReport();
    }
}
