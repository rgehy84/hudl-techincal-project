package pages;

import framework.WebDriverActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    WebDriverActions wpa;


    /* constructor for page factory setup */
    public LoginPage(WebDriver driver) {
        wpa = new WebDriverActions(driver);
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

    /* actions on login page */
    public void enterEmailAddress(String emailAddress) {
        wpa.enterTextIntoInputField(getEmailAddressInputField(), "Email Address", emailAddress);
    }

    public void enterPassword(String password) {
        wpa.enterTextIntoPasswordField(getPasswordInputField(), "Password", password);
    }

    public void clickLoginBtn() {
        wpa.click(getLoginBtn(), "Login button");
    }

    public void loginToHudl(String emailAddress, String password) {
        enterEmailAddress(emailAddress);
        enterPassword(password);
        clickLoginBtn();
    }


}
