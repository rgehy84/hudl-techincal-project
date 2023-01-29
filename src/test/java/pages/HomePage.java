package pages;

import framework.BaseUtilities;
import framework.WebDriverActions;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage extends BaseUtilities {
    WebDriverActions wda;
    WebDriverWait wait;

    public HomePage(WebDriver driver) {
        wda = new WebDriverActions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15), Duration.ofSeconds(50));
        PageFactory.initElements(driver, this);
        print("Go to: " + driver.getCurrentUrl());
    }

    @FindBy(css = "div.mainnav__actions > a[data-qa-id='login']")
    WebElement loginBtn;

    public WebElement getLoginBtn() {
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(loginBtn));

        return loginBtn;
    }

    public void clickLoginBtn() {
        wda.click(getLoginBtn(), "Login button");
    }
}
