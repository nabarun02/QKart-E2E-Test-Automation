package QKART_TESTNG;

import QKART_TESTNG.pages.Checkout;
import QKART_TESTNG.pages.Home;
import QKART_TESTNG.pages.Login;
import QKART_TESTNG.pages.Register;
import QKART_TESTNG.pages.SearchResult;

import static org.testng.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.logging.LogType;
import java.util.logging.Level;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.annotations.Test;

public class QKART_Tests {

    static ChromeDriver driver;
    public static String lastGeneratedUserName;

    @BeforeSuite(alwaysRun = true)
    public static void createDriver() throws MalformedURLException {
        
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log"); 

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();

    }

    /*
     * Testcase01: Verify a new user can successfully register
     */
    @Test(description = "Verify registration happens correctly", priority = 1, groups = {"sanity"})
    @Parameters({"TC1_userName", "TC1_password"})
    public void TestCase01(String TC1_userName, String TC1_password) throws InterruptedException {
         
         Boolean status;
    //   logStatus("Start TestCase", "Test Case 1: Verify User Registration", "DONE");
         takeScreenshot(driver, "StartTestCase", "TestCase1");

        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser(TC1_userName, TC1_password, true);
        assertTrue(status, "Failed to register new user");

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the login page and login with the previuosly registered user
        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, TC1_password);
    //  logStatus("Test Step", "User Perform Login: ", status ? "PASS" : "FAIL");
        assertTrue(status, "Failed to login with registered user");

        // Visit the home page and log out the logged in user
        Home home = new Home(driver);
        status = home.PerformLogout();

    //  logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status
    //  ? "PASS" : "FAIL");
    //  takeScreenshot(driver, "EndTestCase", "TestCase1");
    }
    
    /*
     * Verify that an existing user is not allowed to re-register on QKart
     */
    @Test(description = "Verify re-registering an already registered user fails", priority = 2, groups = {"sanity"})
    public void TestCase02() throws InterruptedException {
        
        Boolean status;
        
        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        Assert.assertTrue(status, "Registration failed!");

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the Registration page and try to register using the previously
        // registered user's credentials
        registration.navigateToRegisterPage();
        status = registration.registerUser(lastGeneratedUserName, "abc@123", false);

        // If status is true, then registration succeeded, else registration has
        // failed. In this case registration failure means Success
       Assert.assertFalse(status, "Re-registration occured!");
        
    }

    /*
     * Verify the functinality of the search text box
     */
    @Test(description = "Verify the functionality of search text box", priority = 3, groups = {"sanity"})
    public void TestCase03() throws InterruptedException {
        
        boolean status;

        // Visit the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for the "yonex" product
        status = homePage.searchForProduct("YONEX");
        Assert.assertTrue(status, "Unable to search for product!");

        // Fetch the search results
        List<WebElement> searchResults = homePage.getSearchResults();

        // Verify the search results are available

        Assert.assertFalse(searchResults.size() == 0, "No result found for the given search string!!");

        for (WebElement webElement : searchResults) {
            // Create a SearchResult object from the parent element
            SearchResult resultelement = new SearchResult(webElement);

            // Verify that all results contain the searched text
            String elementText = resultelement.getTitleofResult();
            
            Assert.assertTrue(elementText.toUpperCase().contains("YONEX"), "Title doesn't contain the search keyword!!");
        
        }

        // Search for product
        status = homePage.searchForProduct("Gesundheit");

        Assert.assertFalse(status, "Invalid keyword returned results!!");

        // Verify no search results are found
        searchResults = homePage.getSearchResults();

        Assert.assertTrue(searchResults.size() == 0, "Some results appeared with invalid keyword!!");

        status = homePage.isNoResultFound();
        Assert.assertTrue(status, "No product found message is not present!!");

    }

    /*
     * Verify the presence of size chart and check if the size chart content is as
     * expected
     */
    @Test(description = "Verify the existence of size chart for certain items and validate contents of size chart", priority = 4, groups = {"regression"})
    public void TestCase04() throws InterruptedException {

        boolean status = false;

        // Visit home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for product and get card content element of search results
        status = homePage.searchForProduct("Running Shoes");
        List<WebElement> searchResults = homePage.getSearchResults();

        // Create expected values
        List<String> expectedTableHeaders = Arrays.asList("Size", "UK/INDIA", "EU", "HEEL TO TOE");
        List<List<String>> expectedTableBody = Arrays.asList(Arrays.asList("6", "6", "40", "9.8"),
                Arrays.asList("7", "7", "41", "10.2"), Arrays.asList("8", "8", "42", "10.6"),
                Arrays.asList("9", "9", "43", "11"), Arrays.asList("10", "10", "44", "11.5"),
                Arrays.asList("11", "11", "45", "12.2"), Arrays.asList("12", "12", "46", "12.6"));

        // Verify size chart presence and content matching for each search result
        for (WebElement webElement : searchResults) {
            SearchResult result = new SearchResult(webElement);

            // Verify if the size chart exists for the search result

            status = result.verifySizeChartExists();
            Assert.assertTrue(status, "Size chart link doesn't exists!!");

            status = result.verifyExistenceofSizeDropdown(driver);
            Assert.assertTrue(status, "Size chart dropdown doesn't exists!!");

            status = result.openSizechart();
            Assert.assertTrue(status, "Failed to open the size chart!!");

            status = result.validateSizeChartContents(expectedTableHeaders, expectedTableBody, driver);
            Assert.assertTrue(status, "Failed to validate size chart content!!");

            status = result.closeSizeChart(driver);
            Assert.assertTrue(status, "Failed to close the size chart");

        }
        
    }

    /*
     * Verify the complete flow of checking out and placing order for products is
     * working correctly
     */
    @Test(description = "Verify that a new user can add multiple products in to the cart and Checkout", priority = 5, groups = {"sanity"})
    @Parameters({"TC5_ProductNameToSearchFor1", "TC5_ProductNameToSearchFor2", "TC5_AddressDetails"})
    public void TestCase05(String TC5_ProductNameToSearchFor1, String TC5_ProductNameToSearchFor2, 
                           String TC5_AddressDetails) throws InterruptedException {
       
        Boolean status;

        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        // Register a new user
        status = registration.registerUser("testUser", "abc@123", true);
        Assert.assertTrue(status, "Registration Failed!");

        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        Login login = new Login(driver);
        login.navigateToLoginPage();

        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        Assert.assertTrue(status, "Login failed!!");

        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Find required products by searching and add them to the user's cart
        status = homePage.searchForProduct("YONEX");
        homePage.addProductToCart(TC5_ProductNameToSearchFor1);
        status = homePage.searchForProduct("Tan");
        homePage.addProductToCart(TC5_ProductNameToSearchFor2);

        // Click on the checkout button
        homePage.clickCheckout();

        // Add a new address on the Checkout page and select it
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress(TC5_AddressDetails);
        checkoutPage.selectAddress(TC5_AddressDetails);

        // Place the order
        checkoutPage.placeOrder();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));

        // Check if placing order redirected to the Thansk page
        status = driver.getCurrentUrl().endsWith("/thanks");
        Assert.assertTrue(status, "Failed to place order!!");

        // Go to the home page
        homePage.navigateToHome();

        // Log out the user
        homePage.PerformLogout();

    }

    /*
     * Verify the quantity of items in cart can be updated
     */
    @Test(description = "Verify that the contents of the cart can be edited", priority = 6, groups = {"regression"})
    @Parameters({"TC6_ProductNameToSearch1", "TC6_ProductNameToSearch2"})
    public void TestCase06(String TC6_ProductNameToSearch1, String TC6_ProductNameToSearch2) throws InterruptedException {
        
        Boolean status;

        Home homePage = new Home(driver);
        Register registration = new Register(driver);
        Login login = new Login(driver);

        registration.navigateToRegisterPage();

        status = registration.registerUser("testUser", "abc@123", true);
        Assert.assertTrue(status, "Registration failed!!");
        
        lastGeneratedUserName = registration.lastGeneratedUsername;

        login.navigateToLoginPage();
        
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        Assert.assertTrue(status, "Login failed!!");

        homePage.navigateToHome();
        status = homePage.searchForProduct("Xtend");
        homePage.addProductToCart(TC6_ProductNameToSearch1);

        status = homePage.searchForProduct("Yarine");
        homePage.addProductToCart(TC6_ProductNameToSearch2);

        // update watch quantity to 2
        homePage.changeProductQuantityinCart(TC6_ProductNameToSearch1, 2);

        // update table lamp quantity to 0
        homePage.changeProductQuantityinCart(TC6_ProductNameToSearch2, 0);

        // update watch quantity again to 1
        homePage.changeProductQuantityinCart(TC6_ProductNameToSearch1, 1);

        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        checkoutPage.placeOrder();

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));
        } catch (TimeoutException e) {
            System.out.println("Error while placing order in: " + e.getMessage());
        }

        status = driver.getCurrentUrl().endsWith("/thanks");
        Assert.assertTrue(status, "Unable to place order!!");

        homePage.navigateToHome();
        homePage.PerformLogout();

    }

    @Test(description = "Verify that insufficient balance error is thrown when the wallet balance is not enough", priority = 7, groups = {"sanity"})
    @Parameters({"TC7_ProductName", "TC7_Qty"})
    public void TestCase07(String TC7_ProductName, int TC7_Qty) throws InterruptedException {
        
        Boolean status;
        
        Register registration = new Register(driver);
        
        registration.navigateToRegisterPage();
        
        status = registration.registerUser("testUser", "abc@123", true);
        Assert.assertTrue(status, "Registration failed!!");

        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        
        login.navigateToLoginPage();
        
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        Assert.assertTrue(status, "Login failed!!");

        Home homePage = new Home(driver);
        homePage.navigateToHome();
        status = homePage.searchForProduct("Stylecon");
        homePage.addProductToCart(TC7_ProductName);

        homePage.changeProductQuantityinCart(TC7_ProductName, TC7_Qty);

        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        checkoutPage.placeOrder();
        Thread.sleep(3000);

        status = checkoutPage.verifyInsufficientBalanceMessage();

        Assert.assertTrue(status, "Insufficient Balance message didn't pop-up!!");

    }

    @Test(description = "Verify that a product added to a cart is available when a new tab is added", priority = 8, groups = {"regression"})
    public void TestCase08() throws InterruptedException {

        Boolean status = false;

        Register registration = new Register(driver);
       
        registration.navigateToRegisterPage();
       
        status = registration.registerUser("testUser", "abc@123", true);
       
        Assert.assertTrue(status, "Registration failed!!");

        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        
        login.navigateToLoginPage();
        
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        
        Assert.assertTrue(status, "Login Failed!!");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        status = homePage.searchForProduct("YONEX");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");

        String currentURL = driver.getCurrentUrl();

        driver.findElement(By.linkText("Privacy policy")).click();
        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);

        driver.get(currentURL);
        Thread.sleep(2000);

        List<String> expectedResult = Arrays.asList("YONEX Smash Badminton Racquet");

        status = homePage.verifyCartContents(expectedResult);
        Assert.assertTrue(status, "Products added to cart is not available when new tab is opened!!");

        driver.close();

        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

    }

    @Test(description = "Verify that privacy policy and about us links are working fine", priority = 9, groups = {"regression"})
    public void TestCase09() throws InterruptedException {
        
        Boolean status = false;

        Register registration = new Register(driver);
        
        registration.navigateToRegisterPage();
        
        status = registration.registerUser("testUser", "abc@123", true);
        Assert.assertTrue(status, "Registration failed!!");

        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        
        login.navigateToLoginPage();
        
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        Assert.assertTrue(status, "Login failed!!");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        String basePageURL = driver.getCurrentUrl();

        driver.findElement(By.linkText("Privacy policy")).click();
        
        // status = driver.getCurrentUrl().equals(basePageURL);
        // Assert.assertTrue(status, "URL didn't change after clicking on privacy policy link!");

        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);
        WebElement PrivacyPolicyHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        
        status = PrivacyPolicyHeading.getText().equals("Privacy Policy");
        Assert.assertTrue(status, "Page heading doesn't start with the Privacy Policy keyword after navigating to Privacy Policy page!");

        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);
        driver.findElement(By.linkText("Terms of Service")).click();

        handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[2]);
        WebElement TOSHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        
        status = TOSHeading.getText().equals("Terms of Service");
        Assert.assertTrue(status, "Page heading doesn't start with the Terms of Service keyword after navigating to Terms of Serive page!");


        driver.close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]).close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

    }

    @Test(description = "Verify that the contact us dialog works fine", priority = 10, groups = {"regression"})
    public void TestCase10() throws InterruptedException {
        

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        driver.findElement(By.xpath("//*[text()='Contact us']")).click();

        WebElement name = driver.findElement(By.xpath("//input[@placeholder='Name']"));
        name.sendKeys("crio user");
        WebElement email = driver.findElement(By.xpath("//input[@placeholder='Email']"));
        email.sendKeys("criouser@gmail.com");
        WebElement message = driver.findElement(By.xpath("//input[@placeholder='Message']"));
        message.sendKeys("Testing the contact us page");

        WebElement contactUs = driver.findElement(
                By.xpath("/html/body/div[2]/div[3]/div/section/div/div/div/form/div/div/div[4]/div/button"));

        contactUs.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.invisibilityOf(contactUs));

        Assert.assertTrue(true);

    }

    @Test(description = "Ensure that the Advertisement Links on the QKART page are clickable", priority = 11, groups = {"sanity"})
    public void TestCase11() throws InterruptedException {
       
        Boolean status = false;

        Register registration = new Register(driver);

        registration.navigateToRegisterPage();

        status = registration.registerUser("testUser", "abc@123", true);
        Assert.assertTrue(status, "Registration failed!");

        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        
        login.navigateToLoginPage();
        
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        Assert.assertTrue(status, "Login failed!");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        status = homePage.searchForProduct("YONEX Smash Badminton Racquet");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");
        homePage.changeProductQuantityinCart("YONEX Smash Badminton Racquet", 1);
        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1  addr Line 2  addr line 3");
        checkoutPage.selectAddress("Addr line 1  addr Line 2  addr line 3");
        checkoutPage.placeOrder();
        Thread.sleep(3000);

        String currentURL = driver.getCurrentUrl();

        List<WebElement> Advertisements = driver.findElements(By.xpath("//iframe"));

        status = Advertisements.size() == 3;
        Assert.assertTrue(status, "3 advertisement are not available!");

        WebElement Advertisement1 = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[1]"));
        driver.switchTo().frame(Advertisement1);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();

        status = !driver.getCurrentUrl().equals(currentURL);
        Assert.assertTrue(status, "Advertisement 1 is not clickable!");

        driver.get(currentURL);
        Thread.sleep(3000);

        WebElement Advertisement2 = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[2]"));
        driver.switchTo().frame(Advertisement2);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();

        status = !driver.getCurrentUrl().equals(currentURL);
        Assert.assertTrue(status, "Advertisement 2 is not clickable!");

    }

    @AfterSuite(alwaysRun = true)
    public static void quitDriver() {
        System.out.println("quit()");
        driver.quit();
    }

    public static void logStatus(String type, String message, String status) {

        System.out.println(String.format("%s |  %s  |  %s | %s", String.valueOf(java.time.LocalDateTime.now()), type,
                message, status));
    }

    public static void takeScreenshot(WebDriver driver, String screenshotType, String description) {
        
        try {

            String basePath = System.getProperty("user.dir") + File.separator + "screenshots";
            
            File theDir = new File(basePath);
            
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            
            String timestamp = String.valueOf(java.time.LocalDateTime.now()).replace(":", "-").replace(".", "-").replace("T", "-");
            String fileName = String.format("screenshot_%s_%s_%s.png", timestamp, screenshotType, description);
            
            TakesScreenshot scrShot = ((TakesScreenshot) driver);
           
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
            File DestFile = new File(basePath + File.separator + fileName);
            
            FileUtils.copyFile(SrcFile, DestFile);

        } 
        
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

