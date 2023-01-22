package testcases;

import framework.Assertions;
import framework.BaseDriver;
import framework.WebDriverActions;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import pages.AuthNavigation;
import pages.HomePage;
import pages.LoginPage;

import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static framework.reporting.extent.ExtentTestManager.startTest;

public class LoginTest extends BaseDriver {
    WebDriverActions wda;
    HomePage home;
    LoginPage login;
    AuthNavigation authNav;
    Assertions assertions;

    @BeforeMethod
    public void setupTests(Method method) {
        wda = new WebDriverActions(getDriver());
        home = new HomePage(getDriver());
        login = new LoginPage(getDriver());
        authNav = new AuthNavigation(getDriver());
        assertions = new Assertions();
        startTest(method.getName(), "Testing: ");
        wda.goToUrl(getUrlToStartTests());
    }

    @Parameters({"username","password"})
    @Test
    public void verifySuccessfulLoginToHudlWithUserNameAndPassword(String username, String password) {
        home.clickLoginBtn();
        login.loginToHudl(username,password);
        assertions.assertEquals("User is successfully logged in", authNav.getExploreLink().getText(), "Explore");
    }

    @Parameters({"username","password"})
    @Test
    public void verifyUserIsSuccessfullyLoggedOutOfHudl(String username, String password) {
        home.clickLoginBtn();
        login.loginToHudl(username,password);
        authNav.logOutOfHudl();
        assertions.assertEquals("Verify that user successfully logged out of Hudl", home.getLoginBtn().getText(), "Log in");
    }


    @Parameters({"reset_password_username"})
    @Test
    public void verifyForgotPasswordFeatureSendsEmail(String username) {
        home.clickLoginBtn();
        login.clickNeedHelpLink();
        login.resetPassword(username);
        assertions.assertEquals("Verify that forgot password successful message is displayed", login.getPasswordResetSuccessfulHeader().getText(), "Check Your Email");
    }


    @Test(dataProvider = "invalid-login-data")
    public void verifyThatUseIsUnableToLoginWhenInvalidLoginDataIsSubmitted(String username, String password, String testDescription) {
        print(testDescription);
        home.clickLoginBtn();
        login.loginToHudl(username, password);
        assertions.assertEquals("Verify unsuccessful login attempt error is displayed", login.getUnsuccessfulLoginAttemptErrorMessage().getText(), "We didn't recognize that email and/or password.Need help?");
        assertions.assertFalse("Verify Log In button is disabled", login.getLoginBtn().isEnabled());
    }

    @Parameters({"username"})
    @Test
    public void validateThatUserIsUnableToLoginAsOrganizationWithInvalidEmailAddress(String username) {
        home.clickLoginBtn();
        login.clickLoginAsOrganizationButton();
        login.logIntoHudlWithYourOrganization(username);
        assertions.assertEquals("Verify user is unable to login as organization with invalid email address", login.getUnsuccessfulLoginAttemptErrorMessage().getText(), "This account can't log in with an organization yet. Please log in using your email and password.");
    }

    @Test
    public void validateThatUserSuccessfullySwitchesBetweenLoginAsOrganizationAndUser() {
        home.clickLoginBtn();
        login.clickLoginAsOrganizationButton();
        assertions.assertEquals("User Successfully Switch to Login as Organization Page", login.getLoginPageTitleHeader().getText(), "Log into Hudl with your Organization");
        login.clickLogInWithEmailAndPassword();
        assertions.assertTrue("Verify Hudl logo is displayed when switching to login with email and password", login.getHudlLogo().isDisplayed());
    }

    @Test
    public void validateLinksOnLoginPageAreNotBroken() {
        home.clickLoginBtn();
        List<WebElement> loginPageLinks = login.findAllLinksOnLoginPage();
        for (int i=0; i < loginPageLinks.size(); i++) {
            WebElement loginPageLink = loginPageLinks.get(i);
            String loginPageLinkUrl = loginPageLink.getAttribute("href");
            verifyLinks(loginPageLinkUrl);
        }
    }

    public void verifyLinks(String linkUrl)
    {
        try
        {
            URL url = new URL(linkUrl);

            //Now we will be creating url connection and getting the response code
            HttpURLConnection httpURLConnect=(HttpURLConnection)url.openConnection();
            httpURLConnect.setConnectTimeout(5000);
            httpURLConnect.connect();

            if(httpURLConnect.getResponseCode()>=400)
            {
                print(linkUrl+" - "+httpURLConnect.getResponseMessage()+"is a broken link");
            }

            //Fetching and Printing the response code obtained
            else{
                System.out.println(linkUrl+" - "+httpURLConnect.getResponseMessage());
            }
            assertions.assertTrue("Validate that response codes are less than 400", httpURLConnect.getResponseCode() < 400, false);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    @DataProvider (name = "invalid-login-data")
    public Object[][] dpMethod(){
        return new Object[][] {
                {"test-valid-email@hudl.com", "test_invalid_password", "User is unable to successfully login with valid username and invalid password"},
                {"test-invalid-email@hudl.com", "test_valid_password", "User is unable to successfully login with valid username and invalid password"},
                {"test-valid-email@hudl.com", "", "User is unable to successfully login with valid username and no password"},
                {"", "test_valid_password", "User is unable to successfully login with no username and valid password"}
        };
    }

}
