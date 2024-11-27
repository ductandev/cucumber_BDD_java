// File: RegisterStepsDefinition.java
package vn.io.ductandev.cucumberBDD.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RegisterStepsDefinition {

    private static final String REGISTER_URL = "http://203.205.26.244:4000/register";
    private static final String LOGINPAGE_URL_FRAGMENT = "/login";
    private static final String EMAIL = "vemose1671@kazvi.com";      // https://10minemail.com
    private static final String PASSWORD = "Ductan123@";
    private static final String USERNAME = "Duc Tan";
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    // Common XPaths
    private static final String XPATH_BUTTON = "//button[contains(text(),'%s')]";
    private static final String XPATH_MESSAGE = "//div[contains(text(),'%s')]";
    private static final String XPATH_ERROR_MESSAGE = "//p[contains(text(),'%s')]";

    // Error Messages
    private static final String ERROR_REGISTER_URL = "Cannot open Register page URL";
    private static final String ERROR_LOGIN_REDIRECT = "Did not redirect correctly to Login";

    private WebDriver driver;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Given("the user is on the registration page")
    public void navigateToRegisterPage() {
        driver.get(REGISTER_URL);
        validateUrl(REGISTER_URL, ERROR_REGISTER_URL);
    }

    @When("the user enters a valid email")
    public void enterValidDetails() {
        enterTextByName("email", EMAIL);
    }

    @And("I click the {string} button")
    public void clickButton(String buttonName) {
        clickButtonByText(buttonName);
    }

    @And("I click the {string} button to verify")
    public void clickButtonVerify(String buttonName) {
        System.out.println("✅ Vui lòng nhập mã xác thực từ gmail: ");
        sleep(30);

        clickButtonByText(buttonName);
        System.out.println("✅ Nhấn nút xác thực thành công: ");
    }

    @And("the user enters a valid password, confirms password, and contactName")
    public void confirmPassword() {
        enterTextByName("password", PASSWORD);
        enterTextByName("rePassword", PASSWORD);
        enterTextByName("contactName", USERNAME);
        System.out.println("✅ Nhập dữ liệu thành công: ");
    }

    @Then("the user should see a message {string}")
    public void validateMessage(String message) {
        waitForTextToBePresent(By.xpath(String.format(XPATH_MESSAGE, message)), message);
        System.out.println("✅ In message thành công: " + message);
    }

    @And("be redirected to the login page")
    public void validateRedirectToLoginPage() {
        waitForUrlToContain(LOGINPAGE_URL_FRAGMENT);
        validateUrlContains(LOGINPAGE_URL_FRAGMENT, ERROR_LOGIN_REDIRECT);
        System.out.println("✅ Chuyển về trang login thành công: ");
    }



    @When("the user enters an invalid email address")
    public void enterInvalidEmail() {
        enterTextByName("email", "invalid-email");
    }

    @Then("the user should see an error validate message {string}")
    public void validateErrorMessage(String message) {
        waitForTextToBePresent(By.xpath(String.format(XPATH_ERROR_MESSAGE, message)), message);
    }


//
//    @When("the user enters a password and a confirmation password that do not match")
//    public void enterNonMatchingPasswords() {
//        enterTextByName("password", "StrongPassword123");
//        enterTextByName("confirmPassword", "DifferentPassword123");
//    }
//
//    @When("the user enters a username that already exists")
//    public void enterExistingUsername() {
//        enterTextByName("username", "existinguser");
//        enterTextByName("email", "existinguser@example.com");  // Replace with valid but existing email
//        enterTextByName("password", "StrongPassword123");
//        enterTextByName("confirmPassword", "StrongPassword123");
//    }
//
//    @When("the user leaves one or more required fields empty")
//    public void leaveRequiredFieldsEmpty() {
//        enterTextByName("username", "");
//        enterTextByName("email", "");
//        enterTextByName("password", "");
//        enterTextByName("confirmPassword", "");
//    }
//
//    @When("the user enters a password that does not meet complexity requirements")
//    public void enterWeakPassword() {
//        enterTextByName("password", "weakpw");
//        enterTextByName("confirmPassword", "weakpw");
//    }
//

//
//    @And("clicks the {string} button")
//    public void clickButton(String buttonName) {
//        clickButtonByText(buttonName);
//    }

    @io.cucumber.java.After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Helper Methods
    private void enterTextByName(String name, String text) {
        driver.findElement(By.name(name)).sendKeys(text);
    }

    private void clickButtonByText(String buttonName) {
        try {
            driver.findElement(By.xpath(String.format(XPATH_BUTTON, buttonName))).click();
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Cannot find button: " + buttonName, e);
        }
    }

    private void validateUrl(String expectedUrl, String errorMessage) {
        if (!Objects.equals(driver.getCurrentUrl(), expectedUrl)) {
            throw new RuntimeException(errorMessage);
        }
    }

    private void validateUrlContains(String expectedUrlFragment, String errorMessage) {
        if (!Objects.requireNonNull(driver.getCurrentUrl()).contains(expectedUrlFragment)) {
            throw new RuntimeException(errorMessage);
        }
    }

    private void waitForUrlToContain(String urlFragment) {
        new WebDriverWait(driver, TIMEOUT).until(ExpectedConditions.urlContains(urlFragment));
    }

    private void waitForElementVisibility(By locator) {
        new WebDriverWait(driver, TIMEOUT)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void waitForTextToBePresent(By locator, String text) {
        try {
            new WebDriverWait(driver, TIMEOUT)
                    .until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        } catch (TimeoutException e) {
            throw new RuntimeException("Error message not found or text did not match: " + text, e);
        }
    }

    private void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds); // delay 15-second
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted during sleep", e);
        }
    }
}