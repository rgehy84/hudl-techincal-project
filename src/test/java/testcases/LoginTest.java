package testcases;

import framework.BaseDriver;
import framework.WebDriverActions;
import org.testng.annotations.*;
import pages.HomePage;
import pages.LoginPage;

public class LoginTest extends BaseDriver {
    WebDriverActions wda;
    HomePage home;
    LoginPage login;


    @BeforeMethod
    public void setupTests() {
        wda = new WebDriverActions(getDriver());
        home = new HomePage(getDriver());
        login = new LoginPage(getDriver());
    }

    @Test
    public void verifySuccessfulLoginToHudlWithUserNameAndPassword() {
        wda.goToUrl(getUrlToStartTests());
        home.clickLoginBtn();
        login.loginToHudl("email goes here","password goes here");

    }

}
