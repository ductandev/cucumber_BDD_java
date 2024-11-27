// File: LoginStepsDefinition.java
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

public class LoginStepsDefinition {

    private static final String LOGIN_URL = "http://203.205.26.244:4000/login";
    private static final String HOMEPAGE_URL_FRAGMENT = "/home";
    private static final String EMAIL = "nguyenductan998@gmail.com";
    private static final String PASSWORD = "Ductan123@";
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    // Common XPaths
    private static final String XPATH_HOME_LINK = "//a[contains(text(),'홈')]";
    private static final String XPATH_BUTTON = "//button[contains(text(),'%s')]";
    private static final String XPATH_ERROR_MESSAGE = "//div[contains(text(),'%s')]";
    private static final String XPATH_ERROR_EMAIL_MESSAGE = "//p[contains(text(),'%s')]";
    private static final String XPATH_ERROR_PASSWORD_MESSAGE = "//p[contains(text(),'%s')]";

    // Error Messages
    private static final String ERROR_LOGIN_URL = "Cannot open Login page URL";
    private static final String ERROR_HOME_REDIRECT = "Did not redirect correctly to Home";
    private static final String ERROR_LOGIN_PAGE = "User did not remain on the login page.";

    private WebDriver driver;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Given("the user is on the login page")
    public void navigateToLoginPage() {
        driver.get(LOGIN_URL);
        validateUrl(LOGIN_URL, ERROR_LOGIN_URL);
    }

    @When("the user enters valid credentials")
    public void enterValidCredentials() {
        enterTextByName("email", EMAIL);
        enterTextByName("password", PASSWORD);
    }

    @And("clicks the {string} button")
    public void clickButton(String buttonName) {
        clickButtonByText(buttonName);
    }

    @Then("the user should be redirected to the home page")
    public void validateRedirectToHomePage() {
        waitForUrlToContain(HOMEPAGE_URL_FRAGMENT);
        validateUrlContains(HOMEPAGE_URL_FRAGMENT, ERROR_HOME_REDIRECT);
        waitForElementVisibility(By.xpath(XPATH_HOME_LINK));
    }

    @When("the user enters a valid username")
    public void enterValidUsername() {
        enterTextByName("email", EMAIL);
    }

    @And("enters an incorrect password")
    public void enterIncorrectPassword() {
        enterTextByName("password", "SaiMatKhau123@"); // Incorrect password
    }

    @Then("the user should see an error message {string}")
    public void validateErrorMessage(String message) {
        waitForTextToBePresent(By.xpath(String.format(XPATH_ERROR_MESSAGE, message)), message);
    }

    @And("the user remains on the login page")
    public void validateUserRemainsOnLoginPage() {
        validateUrl(LOGIN_URL, ERROR_LOGIN_PAGE);
    }

    @When("the user enters a non-existent username")
    public void enterNonExistentUsername() {
        enterTextByName("email", "emailkhongtontai@example.com");
        enterTextByName("password", PASSWORD);
    }

    @And("any password")
    public void enterAnyPassword() {
        enterTextByName("password", "batkyMatKhau123@");
    }


    @When("the user leaves the username or password field empty")
    public void leaveUsernameOrPasswordFieldEmpty() {
        enterTextByName("email", "");
        enterTextByName("password", "");
    }

    @Then("the user should see an error email message {string}")
    public void errorEmailMessage(String message) {
        waitForTextToBePresent(By.xpath(String.format(XPATH_ERROR_EMAIL_MESSAGE, message)), message);
    }

    @And("the user should see an error password message {string}")
    public void errorPasswordMessage(String message) {
        waitForTextToBePresent(By.xpath(String.format(XPATH_ERROR_PASSWORD_MESSAGE, message)), message);
    }

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
        new WebDriverWait(driver, TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void waitForTextToBePresent(By locator, String text) {
        try {
            new WebDriverWait(driver, TIMEOUT)
                    .until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
            System.out.println("Error message displayed correctly: " + text);
        } catch (TimeoutException e) {
            throw new RuntimeException("Error message not found or text did not match: " + text, e);
        }
    }

}