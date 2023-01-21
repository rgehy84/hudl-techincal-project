package testcases;

import framework.BaseDriver;
import framework.WebDriverActions;
import org.openqa.selenium.By;
import org.testng.annotations.*;
public class LoginTest extends BaseDriver {
    private WebDriverActions wda;

    @BeforeMethod
    public void setupTests() {
        wda = new WebDriverActions(getDriver());
    }

    @Test
    public void sampleFrameworkTest() {
        wda.goToUrl("http://www.hudl.com");
        wda.click(getDriver().findElement(By.cssSelector("div.mainnav__actions > a[data-qa-id='login']")), "Login button");
        wda.enterTextIntoInputField(getDriver().findElement(By.id("email")), "Email", "rgehy84@gmail.com");
    }

}
