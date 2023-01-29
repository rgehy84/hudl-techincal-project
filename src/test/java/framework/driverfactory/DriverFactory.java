package framework.driverfactory;

import framework.Browsers;
import framework.RunLocations;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import static framework.Browsers.CHROME;

public class DriverFactory {

    private DriverFactory() {
        //defeat instantiation
    }

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final String ERROR_MSG = "WebDriver instance NOT setup for current thread";

    public static WebDriver getDriver() {
        return Optional.ofNullable(driver.get())
                .orElseThrow(() -> new IllegalStateException(ERROR_MSG));
    }

    public static void setupWebDriver(Browsers browserFlavor, RunLocations runLocation, String gridUrl) throws MalformedURLException {
        driver.set(createBrowserInstance(browserFlavor, runLocation, gridUrl));
    }


    public static void closeBrowser() {
        Optional.ofNullable(driver.get())
                .orElseThrow(() -> new IllegalStateException(ERROR_MSG))
                .quit();
        driver.remove();
    }

    private static WebDriver createBrowserInstance(Browsers browser, RunLocations runLocation, String gridUrl) throws MalformedURLException {
        browser = Optional.ofNullable(browser).orElse(CHROME);
        switch (runLocation) {
            case GRID -> {
                switch (browser) {
                    case CHROME -> {
                        ChromeOptions options = new ChromeOptions();
                        return new RemoteWebDriver(new URL(gridUrl), options);
                    }
                    case FIREFOX -> {
                        FirefoxOptions fOptions = new FirefoxOptions();
                        return new RemoteWebDriver(new URL(gridUrl), fOptions);
                    }
                    case IE -> {
                        InternetExplorerOptions iOptions = new InternetExplorerOptions();
                        return new RemoteWebDriver(new URL(gridUrl), iOptions);
                    }
                    case EDGE -> {
                        EdgeOptions eOptions = new EdgeOptions();
                        return new RemoteWebDriver(new URL(gridUrl), eOptions);
                    }
                    default -> throw new IllegalArgumentException("Browser flavor [" + browser + "] is NOT supported");
                }
            }


            case LOCAL -> {
                switch (browser) {
                    case CHROME -> {
                        WebDriverManager.chromedriver().setup();
                        System.setProperty("webdriver.chrome.silentOutput", "true");
                        ChromeOptions options = new ChromeOptions();
                        options.addArguments("--incognito");
                        return new ChromeDriver(options);
                    }
                    case FIREFOX -> {
                        WebDriverManager.firefoxdriver().setup();
                        FirefoxOptions foptions = new FirefoxOptions();
                        foptions.addArguments("-private");
                        return new FirefoxDriver(foptions);
                    }
                    case IE -> {
                        WebDriverManager.iedriver().setup();
                        InternetExplorerOptions iOptions = new InternetExplorerOptions();
                        iOptions.addCommandSwitches("-private");
                        return new InternetExplorerDriver(iOptions);
                    }
                    case EDGE -> {
                        WebDriverManager.edgedriver().setup();
                        EdgeOptions eOptions = new EdgeOptions();
                        eOptions.addArguments("-private");
                        return new EdgeDriver(eOptions);
                    }
                    case SAFARI -> {
                        WebDriverManager.safaridriver().setup();
                        return new SafariDriver();
                    }
                    default -> throw new IllegalArgumentException("Browser flavor [" + browser + "] is NOT supported");
                }
            }
        }
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver();
    }


}
