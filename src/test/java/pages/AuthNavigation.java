package pages;

import framework.WebDriverActions;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AuthNavigation {
    static WebDriverActions wda;
    static WebDriverWait wait;

    public AuthNavigation(WebDriver driver) {
        wda = new WebDriverActions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15), Duration.ofSeconds(50));
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "a.hui-globalnav__home")
    WebElement homeLink;

    @FindBy(css = "a.hui-globalnav__explore")
    WebElement exploreLink;

    @FindBy(css = "div.hui-globaluseritem__display-name")
    WebElement yourAccountLink;

    @FindBy(xpath = "//a[contains(@class,'hui-globalusermenu__item')]/span[.='Log Out']")
    WebElement logOut;

    public WebElement getHomeLink() {
//        wait.until(ExpectedConditions.stalenessOf(homeLink));
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(homeLink));
        return homeLink;
    }

    public WebElement getExploreLink() {
//        wait.until(ExpectedConditions.stalenessOf(exploreLink));
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(exploreLink));
        return exploreLink;
    }

    public WebElement getYourAccountLink() {
//        wait.until(ExpectedConditions.stalenessOf(yourAccountLink));
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(yourAccountLink));
        return yourAccountLink;
    }

    public WebElement getLogOut() {
//        wait.until(ExpectedConditions.stalenessOf(logOut));
//        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(logOut));
        wait.ignoring(StaleElementReferenceException.class)
                .until((WebDriver d) -> {
                    logOut.isDisplayed();
                    return true;
                });
        return logOut;
    }

    public void logOutOfHudl() {
        wda.click(getYourAccountLink(), "Your Account link in top right corner");
        wda.click(getLogOut(), "Log out link in submenu");
    }
}
