package testcases;

import com.github.hemanthsridhar.CSVUtils;
import com.github.hemanthsridhar.lib.ExtUtils;
import framework.Assertions;
import framework.TestBase;
import framework.WebDriverActions;
import framework.driverfactory.DriverFactory;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.AuthNavigation;
import pages.HomePage;
import pages.LoginPage;

import java.util.List;

public class LoginTest extends TestBase {

    @Parameters({"username","password"})
    @Test(description = "Verify User successfully logs in with valid username and password", priority = 1)
    public void verifySuccessfulLoginToHudlWithUserNameAndPassword(String username, String password) {
        HomePage home = new HomePage(DriverFactory.getDriver());
        LoginPage login = new LoginPage(DriverFactory.getDriver());
        AuthNavigation authNav = new AuthNavigation(DriverFactory.getDriver());
        Assertions assertions = new Assertions(DriverFactory.getDriver());
        home.clickLoginBtn();
        login.loginToHudl(username,password);
        assertions.assertEquals("User is successfully logged in", authNav.getExploreLink().getText(), "Explore");
    }

    @Parameters({"username","password"})
    @Test(description = "Verify user is user successfully logs out of Hudl")
    public void verifyUserIsSuccessfullyLoggedOutOfHudl(String username, String password) {
        HomePage home = new HomePage(DriverFactory.getDriver());
        LoginPage login = new LoginPage(DriverFactory.getDriver());
        AuthNavigation authNav = new AuthNavigation(DriverFactory.getDriver());
        Assertions assertions = new Assertions(DriverFactory.getDriver());
        home.clickLoginBtn();
        login.loginToHudl(username,password);
        authNav.logOutOfHudl();
        assertions.assertEquals("Verify that user successfully logged out of Hudl", home.getLoginBtn().getText(), "Log in");
    }


    @Parameters({"reset_password_username"})
    @Test(enabled = true, description = "Verify forgot password feature shows sent password reset email message", priority = 3)
    public void verifyForgotPasswordFeatureSendsEmail(String username) {
        HomePage home = new HomePage(DriverFactory.getDriver());
        LoginPage login = new LoginPage(DriverFactory.getDriver());
        Assertions assertions = new Assertions(DriverFactory.getDriver());
        home.clickLoginBtn();
        login.clickNeedHelpLink();
        login.resetPassword(username);
        assertions.assertEquals("Verify that forgot password successful message is displayed", login.getPasswordResetSuccessfulHeader().getText(), "Check Your Email");
    }


    @Test(dataProvider = "invalid-login-data",
            description = "Verify user is unable to login with invalid login credentials", priority = 10)
    public void verifyThatUserIsUnableToLoginWhenInvalidLoginDataIsSubmitted(String username, String password, String testDescription) {
        print(testDescription);
        HomePage home = new HomePage(DriverFactory.getDriver());
        LoginPage login = new LoginPage(DriverFactory.getDriver());
        Assertions assertions = new Assertions(DriverFactory.getDriver());
        home.clickLoginBtn();
        login.loginToHudl(username, password);
        assertions.assertEquals("Verify unsuccessful login attempt error is displayed", login.getUnsuccessfulLoginAttemptErrorMessage().getText(), "We didn't recognize that email and/or password.Need help?");
        assertions.assertFalse("Verify Log In button is disabled", login.getLoginBtn().isEnabled());
    }

    @Parameters({"username"})
    @Test(description = "Validate that user is unable to login as organization with invalid organization email address",
    priority = 10)
    public void validateThatUserIsUnableToLoginAsOrganizationWithInvalidEmailAddress(String username) {
        HomePage home = new HomePage(DriverFactory.getDriver());
        LoginPage login = new LoginPage(DriverFactory.getDriver());
        Assertions assertions = new Assertions(DriverFactory.getDriver());
        home.clickLoginBtn();
        login.clickLoginAsOrganizationButton();
        login.logIntoHudlWithYourOrganization(username);
        assertions.assertEquals("Verify user is unable to login as organization with invalid email address", login.getUnsuccessfulLoginAttemptErrorMessage().getText(), "This account can't log in with an organization yet. Please log in using your email and password.");
    }

    @Test(description = "Validate that user successfully switches between login as organization and " +
            "login with email and password functionality", priority = 5)
    public void validateThatUserSuccessfullySwitchesBetweenLoginAsOrganizationAndUser() {
        WebDriverActions wda = new WebDriverActions(DriverFactory.getDriver());
        HomePage home = new HomePage(DriverFactory.getDriver());
        LoginPage login = new LoginPage(DriverFactory.getDriver());
        Assertions assertions = new Assertions(DriverFactory.getDriver());
        wda.goToUrl(getUrlToStartTests());
        home.clickLoginBtn();
        login.clickLoginAsOrganizationButton();
        assertions.assertEquals("User Successfully Switch to Login as Organization Page", login.getLoginPageTitleHeader().getText(), "Log into Hudl with your Organization");
        login.clickLogInWithEmailAndPassword();
        assertions.assertTrue("Verify Hudl logo is displayed when switching to login with email and password", login.getHudlLogo().isDisplayed());
    }

    @Test(description = "Validate links on Login page are not broken", priority = 6)
    public void validateLinksOnLoginPageAreNotBroken() {
        HomePage home = new HomePage(DriverFactory.getDriver());
        LoginPage login = new LoginPage(DriverFactory.getDriver());
        Assertions assertions = new Assertions(DriverFactory.getDriver());
        home.clickLoginBtn();
        List<WebElement> loginPageLinks = login.findAllLinksOnLoginPage();
        for (WebElement loginPageLink : loginPageLinks) {
            String loginPageLinkUrl = loginPageLink.getAttribute("href");
            assertions.verifyLinks(loginPageLinkUrl);
        }
    }

    @DataProvider(name = "invalid-login-data", parallel = true)
    public Object[][] csvDataRead() throws Exception {
        String path = "test-data/invalid-login-credentials.csv";
        ExtUtils ext = new CSVUtils(path);
/*
If there are column names in the first row,
ExtUtils ext = new CSVUtils(path, true);
*/
        return ext.parseData();
    }

}
