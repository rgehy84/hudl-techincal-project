package pages;

import framework.WebDriverActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
    WebDriverActions wpa;

    public HomePage(WebDriver driver) {
        wpa = new WebDriverActions(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "div.mainnav__actions > a[data-qa-id='login']")
    WebElement loginBtn;

    public WebElement getLoginBtn() {
        return loginBtn;
    }

    public void clickLoginBtn() {
        wpa.click(getLoginBtn(), "Login button");
    }
}
