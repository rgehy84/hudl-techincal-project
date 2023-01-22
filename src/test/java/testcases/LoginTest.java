package testcases;

import com.github.hemanthsridhar.CSVUtils;
import com.github.hemanthsridhar.lib.ExtUtils;
import framework.Assertions;
import framework.BaseDriver;
import framework.WebDriverActions;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import pages.AuthNavigation;
import pages.HomePage;
import pages.LoginPage;
import java.util.List;

public class LoginTest extends BaseDriver {
    WebDriverActions wda;
    HomePage home;
    LoginPage login;
    AuthNavigation authNav;
    Assertions assertions;

    @BeforeMethod
    public void setupTests() {
        wda = new WebDriverActions(getDriver());
        home = new HomePage(getDriver());
        login = new LoginPage(getDriver());
        authNav = new AuthNavigation(getDriver());
        assertions = new Assertions();
    }

    @Parameters({"username","password"})
    @Test(description = "Verify User successfully logs in with valid username and password", priority = 1)
    public void verifySuccessfulLoginToHudlWithUserNameAndPassword(String username, String password) {
        wda.goToUrl(getUrlToStartTests());
        home.clickLoginBtn();
        login.loginToHudl(username,password);
        assertions.assertEquals("User is successfully logged in", authNav.getExploreLink().getText(), "Explore");
    }

    @Parameters({"username","password"})
    @Test(description = "Verify user is user successfully logs out of Hudl", priority = 2)
    public void verifyUserIsSuccessfullyLoggedOutOfHudl(String username, String password) {
        wda.goToUrl(getUrlToStartTests());
        home.clickLoginBtn();
        login.loginToHudl(username,password);
        authNav.logOutOfHudl();
        assertions.assertEquals("Verify that user successfully logged out of Hudl", home.getLoginBtn().getText(), "Log in");
    }


    @Parameters({"reset_password_username"})
    @Test(description = "Verify forgot password feature shows sent password reset email message", priority = 3)
    public void verifyForgotPasswordFeatureSendsEmail(String username) {
        wda.goToUrl(getUrlToStartTests());
        home.clickLoginBtn();
        login.clickNeedHelpLink();
        login.resetPassword(username);
        assertions.assertEquals("Verify that forgot password successful message is displayed", login.getPasswordResetSuccessfulHeader().getText(), "Check Your Email");
    }


    @Test(dataProvider = "invalid-login-data",
            description = "Verify user is unable to login with invalid login credentials", priority = 10)
    public void verifyThatUserIsUnableToLoginWhenInvalidLoginDataIsSubmitted(String username, String password, String testDescription) {
        print(testDescription);
        wda.goToUrl(getUrlToStartTests());
        home.clickLoginBtn();
        login.loginToHudl(username, password);
        assertions.assertEquals("Verify unsuccessful login attempt error is displayed", login.getUnsuccessfulLoginAttemptErrorMessage().getText(), "We didn't recognize that email and/or password.Need help?");
        assertions.assertFalse("Verify Log In button is disabled", login.getLoginBtn().isEnabled());
    }

    @Parameters({"username"})
    @Test(description = "Validate that user is unable to login as organization with invalid organization email address",
    priority = 10)
    public void validateThatUserIsUnableToLoginAsOrganizationWithInvalidEmailAddress(String username) {
        wda.goToUrl(getUrlToStartTests());
        home.clickLoginBtn();
        login.clickLoginAsOrganizationButton();
        login.logIntoHudlWithYourOrganization(username);
        assertions.assertEquals("Verify user is unable to login as organization with invalid email address", login.getUnsuccessfulLoginAttemptErrorMessage().getText(), "This account can't log in with an organization yet. Please log in using your email and password.");
    }

    @Test(description = "Validate that user successfully switches between login as organization and " +
            "login with email and password functionality", priority = 5)
    public void validateThatUserSuccessfullySwitchesBetweenLoginAsOrganizationAndUser() {
        wda.goToUrl(getUrlToStartTests());
        home.clickLoginBtn();
        login.clickLoginAsOrganizationButton();
        assertions.assertEquals("User Successfully Switch to Login as Organization Page", login.getLoginPageTitleHeader().getText(), "Log into Hudl with your Organization");
        login.clickLogInWithEmailAndPassword();
        assertions.assertTrue("Verify Hudl logo is displayed when switching to login with email and password", login.getHudlLogo().isDisplayed());
    }

    @Test(description = "Validate links on Login page are not broken", priority = 6)
    public void validateLinksOnLoginPageAreNotBroken() {
        wda.goToUrl(getUrlToStartTests());
        home.clickLoginBtn();
        List<WebElement> loginPageLinks = login.findAllLinksOnLoginPage();
        for (WebElement loginPageLink : loginPageLinks) {
            String loginPageLinkUrl = loginPageLink.getAttribute("href");
            assertions.verifyLinks(loginPageLinkUrl);
        }
    }

    @DataProvider(name = "invalid-login-data")
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
