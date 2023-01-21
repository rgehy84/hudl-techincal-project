package framework;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.*;

import java.time.Duration;

public class BaseDriver extends BaseUtilities {
    WebDriver driver;

    /***
     * maximizes the browser window and sets in implicit wait of 15 seconds before timing out
     * @return driver that can be used in tests without making the original driver accessible to the public
     */
    public WebDriver getDriver() {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        return driver;
    }


    /***
     * this uses the WebDriverManager to set the driver binaries. It avoids having to
     * manually maintain the driver binaries. This will also download the latest version of the drivers
     * @param browser => specifies the browser to prepare to testing
     */
    @Parameters("browser_to_run_tests")
    @BeforeClass
    static void setupClass(@Optional("CHROME") Browsers browser) {
        switch (browser) {
            case FIREFOX -> WebDriverManager.firefoxdriver().setup();
            case CHROME -> WebDriverManager.chromedriver().setup();
            case SAFARI -> WebDriverManager.safaridriver().setup();
            case IE -> WebDriverManager.iedriver().setup();
            case EDGE -> WebDriverManager.edgedriver().setup();
        }

    }

    /***
     * this method is used to create a new instance of the driver
     * @param browser => specifies which browser to run the tests
     */
    @Parameters("browser_to_run_tests")
    @BeforeMethod
    public void testSetup(@Optional("CHROME") Browsers browser) {
        switch (browser) {
            case FIREFOX -> driver = new FirefoxDriver();
            case CHROME -> driver = new ChromeDriver();
            case SAFARI -> driver = new SafariDriver();
            case IE -> driver = new InternetExplorerDriver();
            case EDGE -> driver = new EdgeDriver();
        }
        print("Tests will be executed in " + browser.name() + " browser");
    }

    /***
     * this method is used to tear down the webdriver instance after each method is executed
     */
    @AfterMethod
    public void teardownDriver() {
        getDriver().close();
    }

}
