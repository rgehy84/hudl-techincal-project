package framework;

import framework.driverfactory.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.net.MalformedURLException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class TestBase extends BaseUtilities {

    private static String urlToStartTests;
    private static Browsers browserToRunTest;
    private static RunLocations runLocationOfTests;
    private static String gridUrl;

    /**
     * Set up the driver based on the browser name parameter passed from TestNG XML to the DriverFactory to setup WebDriver.
     * Navigates to the url set in the TestNG XML file
     */
    @BeforeMethod(dependsOnMethods = "getUrlToStartTests")
    public void LaunchApplication() throws MalformedURLException {
        DriverFactory.setupWebDriver(getBrowserToRunTest(), getRunLocationOfTests(), getGridUrl());
        String url = getUrlToStartTests();
        WebDriver driver = DriverFactory.getDriver();
        driver.get(url);
        driver.manage().window().maximize();
        System.out.println("Browser maximized");
        driver.manage().timeouts().implicitlyWait(Duration.of(30, ChronoUnit.SECONDS));
    }


    /***
     * this method is used to tear down the webdriver instance after each method is executed
     * @param shouldCloseBrowserAfterTestExecution -> boolean value, defaults to true to close
     *                                             browser after execution;
     *                                             set to false to keep browser open
     */
    @Parameters({"close_browser_after_after_test_run"})
    @AfterMethod
    public void tearDown(boolean shouldCloseBrowserAfterTestExecution) {
        if (shouldCloseBrowserAfterTestExecution) {
            if (DriverFactory.getDriver() != null) {
                DriverFactory.closeBrowser();
            }
        } else {
            print("Parameter passed to keep browser open");
        }

    }

    /***
     * This sets the URL that the tests should be executed. It passes the value from the TestNG XML parameter
     * @param urlToStartTest Set based on the parameter "url_to_execute_tests". Defaults to http://www.hudl.com/
     */
    @Parameters("url_to_execute_tests")
    @BeforeMethod(dependsOnMethods = "setBrowserToRunTest")
    public void setUrlToTest(@Optional("http://www.hudl.com/") String urlToStartTest) {
        this.urlToStartTests = urlToStartTest;
    }

    /***
     * Used to call the URL that's set to execute the tests. Useful if you need the URL within the building of the test cases
     * i.e. comparing the url in the browser vs the url passed
     * @return urlToStartTests that is set within the TestNG XML file parameter "browser_to_run_tests"
     */
    @BeforeMethod(dependsOnMethods = "setUrlToTest")
    public final String getUrlToStartTests() {
        return urlToStartTests;
    }

    /***
     * Provides the browser to run the tests against that is specified in the TestNG XML file
     * @return browserToRunTest based on the TestNG parameter "browser_to_run_tests". Defaults to CHROME
     */
    public static Browsers getBrowserToRunTest() {
        return browserToRunTest;
    }

    /***
     * Used to set the browser to execute the test. Uses the Browers ENUM.
     * @param browserToRunTest The options are CHROME, FIREFOX, IE, EDGE, SAFARI
     */
    @Parameters({"browser_to_run_tests"})
    @BeforeMethod(dependsOnMethods = "getGridUrl")
    public void setBrowserToRunTest(Browsers browserToRunTest) {
        this.browserToRunTest = browserToRunTest;
    }

    /***
     * Used to pass whether tests should run on the Local device or a selenium grid from parameters in tests
     * @return returns the test run location from the TestNG parameter "run_test_local_or_grid"
     */
    @BeforeMethod
    public static RunLocations getRunLocationOfTests() {
        return runLocationOfTests;
    }

    /***
     * Used to set whether to run selenium tests locally or on a selenium grid
     * @param runLocationOfTests passes value from TestNG XML parameter "run_test_local_or_grid" from RunLocations enum
     *                           takes the value of LOCAL or GRID. Value is defaulted to local
     */
    @Parameters({"run_test_local_or_grid"})
    @BeforeMethod
    public static void setRunLocationOfTests(@Optional("LOCAL") RunLocations runLocationOfTests) {
        TestBase.runLocationOfTests = runLocationOfTests;
    }

    /***
     * Used to pass grid url set from parameters in tests
     * @return returns the grid url from the TestNG parameter "selenium_grid_url"
     */
    @BeforeMethod
    public static String getGridUrl() {
        return gridUrl;
    }

    /***
     * Specifies the selenium grid url to execute the tests. i.e. "http://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:80/wd/hub"
     * @param gridUrl defaults to local selenium grid installation that can be running selenium hub from docker
     */
    @Parameters({"selenium_grid_url"})
    @BeforeMethod(dependsOnMethods = "getRunLocationOfTests")
    public static void setGridUrl(@Optional("http://127.0.0.1:4444") String gridUrl) {
        TestBase.gridUrl = gridUrl;
    }
}
