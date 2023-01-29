package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class UserHomePage {
    public UserHomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}
