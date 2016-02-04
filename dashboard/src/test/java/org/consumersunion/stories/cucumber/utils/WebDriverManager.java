package org.consumersunion.stories.cucumber.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Strings;

/**
 * Manages the Selenium {@link WebDriver}s used on the acceptance tests.
 * <p/>
 * Most of the methods in this class could be made static, however, we look ahead to the possibility of running multiple
 * tests in parallel and so implement as instance methods to reduce future changes.
 */
public class WebDriverManager {
    private static ChromeDriverService driverService;
    protected static WebDriver driver;

    public void setupDriver() throws IOException {
        // Note: set this as an OS environmental variable.
        String pathToChromeWebDriver = System.getProperty("WEBDRVIER_DRIVER_CHROME");

        if (!Strings.isNullOrEmpty(pathToChromeWebDriver)) {
            System.setProperty("webdriver.chrome.driver", pathToChromeWebDriver);
            driverService = new ChromeDriverService.Builder().
                    usingDriverExecutable(new File(pathToChromeWebDriver)).
                    usingAnyFreePort().
                    build();
            driverService.start();
            driver = new RemoteWebDriver(driverService.getUrl(), DesiredCapabilities.chrome());
        } else { // nothing to do; FF does not require a service, just the driver
            driver = new FirefoxDriver();
        }

        driver.manage().timeouts().implicitlyWait(TestParameters.IMPLICIT_WAIT, TimeUnit.MILLISECONDS);
    }

    public void tearDownDriver() {
        driver.quit();
        if (driverService != null) {
            driverService.stop();
        }
    }

    public WebDriver getWebDriver() {
        return driver;
    }

    public WebDriverWait getDefaultWait() {
        return new WebDriverWait(driver, 30);
    }

    public void clearAllCookies() {
        driver.manage().deleteAllCookies();
    }

    public boolean waitUntilDomIsLoaded(By condition) {
        try {
            final WebDriverWait wait = new WebDriverWait(driver, TestParameters.TIME_OUT_IN_SECONDS);
            wait.until(ExpectedConditions.presenceOfElementLocated(condition));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void waitUntilInstancesAreEqualsTo(final By elementLocator, final int instances) {
        getDefaultWait().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return (driver.findElements(elementLocator).size() == instances);
            }
        });
    }

    public void waitUntilInstancesAreMoreThan(final By elementLocator, final int instances) {
        getDefaultWait().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return (driver.findElements(elementLocator).size() > instances);
            }
        });
    }

    public void waitUntilInstancesAreLessThan(final By elementLocator, final int instances) {
        getDefaultWait().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return (driver.findElements(elementLocator).size() < instances);
            }
        });
    }

    public boolean isElementDisplayed(By locator) {
        if (!driver.findElements(locator).isEmpty()) {
            return driver.findElement(locator).isDisplayed();
        } else {
            return false;
        }
    }

    public int getElementCount(By locator) {
        List<WebElement> elementsFound = driver.findElements(locator);
        return elementsFound.size();
    }

    /**
     * Find out if an element is stale or not.
     *
     * @param element - An element locator
     * @return boolean - True if element location is found, otherwise false.
     * @throws Exception
     */
    public boolean isElementStale(WebElement element) {
        try {
            element.getLocation();
        } catch (StaleElementReferenceException Ex) {
            return true;
        }
        return false;
    }

    public Boolean waitUntilTitleContains(String title) {
        try {
            getDefaultWait().until(
                    ExpectedConditions.titleContains(title));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public Boolean waitUntilTitleIs(String title) {
        try {
            getDefaultWait().until(
                    ExpectedConditions.titleIs(title));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getPageTitle() throws Exception {
        String title = "";
        try {
            title = driver.getTitle();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
        return title;
    }

    public WebElement FindElement(By locator) {
        WebElement wElement = driver.findElement(locator);
        return wElement;
    }

    public List<WebElement> FindElements(By locator) {
        List<WebElement> ListElements = driver.findElements(locator);
        return ListElements;
    }

    public void Click(By locator) {
        WebElement wElement = driver.findElement(locator);
        wElement.click();
    }

    public void Click(WebElement wElement) {
        wElement.click();
    }

    public void check(By locator) throws Exception {
        WebElement checkBox = driver.findElement(locator);
        verifyCheckbox(checkBox);
        if (!checkBox.isSelected()) {
            checkBox.click();
        }
    }

    public void uncheck(By locator) throws Exception {
        WebElement checkBox = driver.findElement(locator);
        verifyCheckbox(checkBox);
        if (checkBox.isSelected()) {
            checkBox.click();
        }
    }

    private void verifyCheckbox(WebElement el) throws Exception {
        if (!el.getAttribute("type").toLowerCase().equals("checkbox")) {
            throw new Exception("This elementLocator is not a checkbox!");
        }
    }

    public boolean isChecked(By locator) throws Exception {
        WebElement checkBox = driver.findElement(locator);
        if (!checkBox.getAttribute("type").toLowerCase().equals("checkbox")) {
            throw new Exception("This elementLocator is not a checkbox!");
        }

        return checkBox.getAttribute("checked").equals("checked");
    }

    public void clickAndHold(By locator) {
        WebElement webElement = driver.findElement(locator);

        if (webElement != null) {
            new Actions(driver).clickAndHold(webElement).perform();
        }
    }

    public void clickAndHold(WebElement webElement) {
        if (webElement != null) {
            new Actions(driver).clickAndHold(webElement).perform();
        }
    }

    public void releaseElement(By locator) {
        WebElement webElement = driver.findElement(locator);

        if (webElement != null) {
            new Actions(driver).release(webElement).perform();
        }
    }

    public void releaseElement(WebElement webElement) {
        if (webElement != null) {
            new Actions(driver).release(webElement).perform();
        }
    }

    public void submitElement(By locator) {
        WebElement webElement = driver.findElement(locator);

        if (webElement != null) {
            webElement.submit();
        }
    }

    public void submitElement(WebElement webElement) {
        if (webElement != null) {
            webElement.submit();
        }
    }

    public void sendKeys(By locator, String text) {
        WebElement webElement = driver.findElement(locator);
        webElement.sendKeys(text);
    }

    public void sendKeys(By locator, Keys keys) {
        WebElement webElement = driver.findElement(locator);
        webElement.sendKeys(keys);
    }

    public void sendKeys(WebElement wElement, String text) {
        if (wElement != null) {
            wElement.sendKeys(text);
        }
    }

    public void sendKeysWithClearTextbox(WebElement wElement, String text) {
        if (wElement != null) {
            wElement.clear();
            wElement.sendKeys(text);
        }
    }

    public void sendKeysWithClearTextbox(By locator, String text) {
        WebElement webElement = driver.findElement(locator);

        if (webElement != null) {
            webElement.clear();
            webElement.sendKeys(text);
        }
    }

    public void clearTextbox(WebElement wElement) {
        if (wElement != null) {
            wElement.clear();
        }
    }

    public void clearTextbox(By locator) {
        WebElement webElement = driver.findElement(locator);

        if (webElement != null) {
            webElement.clear();
        }
    }

    public void selectByValue(By locator, String value) {
        Select select = (Select) driver.findElement(locator);
        select.selectByValue(value);
    }

    public void selectByIndex(By locator, int index) {
        Select select = (Select) driver.findElement(locator);
        select.selectByIndex(index);
    }

    public void selectByVisibleText(By locator, String text) {
        Select select = (Select) driver.findElement(locator);
        select.selectByVisibleText(text);
    }

    public void Pause(int ms) throws InterruptedException {
        Thread.sleep(ms);
    }

    public static void navigateURL(String url) {
        driver.navigate().to(url);
    }

    public static String getTestUrlBase() {
        String cukeUrlBase = System.getProperty("CUKE_URL_BASE");
        if ("${CUKE_URL_BASE}".equals(cukeUrlBase) || Strings.isNullOrEmpty(cukeUrlBase)) {
            cukeUrlBase = "http://127.0.0.1:8080";
        }
        return cukeUrlBase;
    }

    public String getUrlWindow() {
        return driver.getCurrentUrl();
    }

    public void switchToMainWindow() {
        driver.switchTo().defaultContent();
    }

    public void switchToNewWindow() {
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
    }

    public void dragAndDrop(By fromLocator, By toLocator) {
        WebElement fromElement = driver.findElement(fromLocator);
        WebElement toElement = driver.findElement(toLocator);

        Actions builder = new Actions(driver);
        builder.dragAndDrop(fromElement, toElement);
        builder.perform();
    }

    public void dragAndDrop(WebElement fromElement, WebElement toElement) {
        Actions builder = new Actions(driver);
        builder.dragAndDrop(fromElement, toElement);
        builder.perform();
    }

    public void dragAndDropByActions(By fromLocator, By toLocator) {
        WebElement fromElement = driver.findElement(fromLocator);
        WebElement toElement = driver.findElement(toLocator);

        Actions builder = new Actions(driver);
        builder.clickAndHold(fromElement).moveToElement(toElement)
                .release(fromElement);
        builder.perform();
    }

    public void dragAndDropByActions(WebElement fromElement, WebElement toElement) {
        Actions builder = new Actions(driver);
        builder.clickAndHold(fromElement).moveToElement(toElement)
                .release(fromElement);
        builder.perform();
    }

    public void waitUntilElementIsDisplayed(By selector) {
        getDefaultWait().until(ExpectedConditions.visibilityOfElementLocated(selector));
    }
}

