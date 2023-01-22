package pages;

import framework.WebDriverActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AuthNavigation {
    WebDriverActions wda;

    public AuthNavigation(WebDriver driver) {
        wda = new WebDriverActions(driver);
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
        return homeLink;
    }

    public WebElement getExploreLink() {
        return exploreLink;
    }

    public WebElement getYourAccountLink() {
        return yourAccountLink;
    }

    public WebElement getLogOut() {
        return logOut;
    }

    public void logOutOfHudl() {
        wda.click(getYourAccountLink(), "Your Account link in top right corner");
        wda.click(getLogOut(), "Log out link in submenu");
    }
}
