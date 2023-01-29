package pages;

import framework.BaseUtilities;
import framework.WebDriverActions;
import framework.driverfactory.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LoginPage extends BaseUtilities {
    WebDriverActions wda;
    WebDriverWait wait;


    /* constructor for page factory setup */
    public LoginPage(WebDriver driver) {
        wda = new WebDriverActions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15), Duration.ofSeconds(50));
        PageFactory.initElements(driver, this);
    }

    /* page factory of login page elements */
    @FindBy(id = "email")
    WebElement emailAddressInputField;

    @FindBy(id = "password")
    WebElement passwordInputField;

    @FindBy(id = "logIn")
    WebElement loginBtn;

    @FindBy(css = "a[data-qa-id='need-help-link']")
    WebElement needHelpLink;

    @FindBy(css = "label[data-qa-id='remember-me-checkbox-label']")
    WebElement rememberMeCheckbox;

    @FindBy(css = "button[data-qa-id='log-in-with-organization-btn']")
    WebElement logInWithOrganizationBtn;

    @FindBy(css = "a[class*='styles_signUpLink']")
    WebElement signUpLink;

    @FindBy(css = "a[class*='styles_hudlLogoContainer']")
    WebElement hudlLogo;

    @FindBy(css = "input[data-qa-id='password-reset-input']")
    WebElement passwordResetInputField;

    @FindBy(css = "button[data-qa-id='password-reset-submit-btn']")
    WebElement submitPasswordResetButton;

    @FindBy(css = "h3.uni-headline")
    WebElement passwordResetSuccessfulHeader;



    @FindBy(css = "p[data-qa-id='error-display']")
    WebElement unsuccessfulLoginAttemptErrorMessage;

    @FindBy(name = "username")
    WebElement organizationEmailAddressField;

    @FindBy(css = "button[data-qa-id='log-in-with-sso']")
    WebElement organizationLogInButton;

    @FindBy(css = "button[data-qa-id='log-in-with-email-and-password']")
    WebElement logInWithEmailAndPasswordButton;

    @FindBy(css = "h2.uni-headline")
    WebElement loginPageTitleHeader;


    public WebElement getUnsuccessfulLoginAttemptErrorMessage() {
        wait.until(ExpectedConditions.visibilityOf(unsuccessfulLoginAttemptErrorMessage));
        return unsuccessfulLoginAttemptErrorMessage;
    }

    public WebElement getPasswordResetSuccessfulHeader() {
        wait.until(ExpectedConditions.visibilityOf(passwordResetSuccessfulHeader));
        return passwordResetSuccessfulHeader;
    }

    public WebElement getPasswordResetInputField() {
        wait.until(ExpectedConditions.visibilityOf(passwordResetInputField));
        return passwordResetInputField;
    }

    public WebElement getSubmitPasswordResetButton() {
        return submitPasswordResetButton;
    }

    /* getters */
    public WebElement getEmailAddressInputField() {
        return emailAddressInputField;
    }

    public WebElement getPasswordInputField() {
        return passwordInputField;
    }

    public WebElement getLoginBtn() {
        return loginBtn;
    }

    public WebElement getNeedHelpLink() {
        return needHelpLink;
    }

    public WebElement getRememberMeCheckbox() {
        return rememberMeCheckbox;
    }

    public WebElement getLogInWithOrganizationBtn() {
        return logInWithOrganizationBtn;
    }

    public WebElement getSignUpLink() {
        return signUpLink;
    }

    public WebElement getHudlLogo() {
        return hudlLogo;
    }

    public WebElement getOrganizationEmailAddressField() {
        return organizationEmailAddressField;
    }

    public WebElement getOrganizationLogInButton() {
        return organizationLogInButton;
    }

    /* actions on login page */
    public void enterEmailAddress(String emailAddress) {
        wda.enterTextIntoInputField(getEmailAddressInputField(), "Email Address", emailAddress);
    }

    public void enterPassword(String password) {
        wda.enterTextIntoPasswordField(getPasswordInputField(), "Password", password);
    }

    public void clickLoginBtn() {
        wda.click(getLoginBtn(), "Login button");
    }

    public WebElement getLogInWithEmailAndPasswordButton() {
        return logInWithEmailAndPasswordButton;
    }

    public WebElement getLoginPageTitleHeader() {
        wait.until(ExpectedConditions.visibilityOf(loginPageTitleHeader));
        return loginPageTitleHeader;
    }

    public void loginToHudl(String emailAddress, String password) {
        enterEmailAddress(emailAddress);
        enterPassword(password);
        clickLoginBtn();
    }

    public void clickNeedHelpLink() {
        wda.click(getNeedHelpLink(), "Need Help? link");
    }

    public void enterPasswordResetEmail(String emailAddress) {
        wda.enterTextIntoInputField(getPasswordResetInputField(), "Password reset email address", emailAddress);
    }

    public void clickSendPasswordResetButton() {
        wda.click(getSubmitPasswordResetButton(), "Send Password Reset button");
    }

    public void resetPassword(String emailAddress) {
        enterPasswordResetEmail(emailAddress);
        clickSendPasswordResetButton();
        wait.until(ExpectedConditions.visibilityOf(passwordResetSuccessfulHeader));
    }

    public void clickLoginAsOrganizationButton() {
        wda.click(getLogInWithOrganizationBtn(), "Log In with an Organization button");
    }

    public void logIntoHudlWithYourOrganization(String organizationEmailAddress) {
        wda.enterTextIntoInputField(getOrganizationEmailAddressField(), "Email ", organizationEmailAddress);
        wda.click(getOrganizationLogInButton(), "Log In button");
    }

    public void clickLogInWithEmailAndPassword() {
        wda.click(getLogInWithEmailAndPasswordButton(), "Log In with Email and Password");
    }

    public List<WebElement> findAllLinksOnLoginPage() {
        List<WebElement> links = DriverFactory.getDriver().findElements(By.tagName("a"));
        print("No of links on Login Page: " + links.size());
        return links;
    }

}
