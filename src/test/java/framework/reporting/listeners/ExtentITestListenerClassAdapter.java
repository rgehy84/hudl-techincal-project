package framework.reporting.listeners;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import framework.BaseDriver;
import framework.BaseUtilities;
import framework.reporting.extent.ExtentService;
import framework.reporting.extent.ExtentTestManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import static framework.reporting.extent.ExtentTestManager.getTest;

public class ExtentITestListenerClassAdapter implements ITestListener {
    BaseDriver baseDriver = new BaseDriver();

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    /**
     * overriding the on start method to use the customized ExtentService method
     * @param context
     */
    public synchronized void onStart(ITestContext context) {
        ExtentService.getInstance().setAnalysisStrategy(AnalysisStrategy.CLASS);
        ExtentService.getInstance().setSystemInfo("Operating System",System.getProperty("os.name"));
        ExtentService.getInstance().setSystemInfo("User",System.getProperty("user.name"));
        if(context.getSuite().getParameter("application") != null) {
            ExtentService.getInstance().setSystemInfo("Application", context.getSuite().getParameter("application"));
        }
        if(context.getSuite().getParameter("type") != null) {
            ExtentService.getInstance().setSystemInfo("Type", context.getSuite().getParameter("type"));
        }


    }

    public synchronized void onTestStart(ITestResult result) {
        baseDriver.print(getTestMethodName(result) + " test is starting.");
    }

    /**
     * overriding the on finish method to use the customized ExtentService method
     * @param context
     */
    public synchronized void onFinish(ITestContext context) {
        ExtentService.getInstance().flush();
    }

    /**
     * takes a base64 screenshot on test failure and adds it to the report
     * @param result
     */
    public synchronized void onTestFailure(ITestResult result) {
        getTest().log(Status.FAIL, "<style=\"color:red; font-weight:bold;\">Failure URL:</style> <a target=\"_blank\" href=\"" + baseDriver.getDriver().getCurrentUrl() + "\">" +  baseDriver.getDriver().getCurrentUrl() + "</a>");
        getTest().log(Status.FAIL, "Screenshot",
                MediaEntityBuilder.createScreenCaptureFromBase64String(base64conversion(baseDriver.getDriver())).build());


        getTest().log(Status.FAIL, "Test Failed");
    }

    /**
     * logs test success method to report
     * @param result
     */
    public synchronized void onTestSuccess(ITestResult result) {
        getTest().log(Status.PASS, "Test Passed");
    }

    /**
     * logs skipped method to report
     * @param result
     */
    public synchronized void onTestSkipped(ITestResult result) {
        getTest().log(Status.SKIP, "Test Skipped");
    }

    /**
     * converts a screenshot to base64
     * @param driver passes the driver in order to get the screenshot
     * @return the base 64 version of the screenshot
     */
    private String base64conversion(WebDriver driver) {
        TakesScreenshot newScreen = (TakesScreenshot) driver;
        String scnShot = newScreen.getScreenshotAs(OutputType.BASE64);
        return scnShot ;
    }

    private byte[] byteConversion(WebDriver driver) {
        TakesScreenshot newScreen = (TakesScreenshot) driver;
        byte[] scnShot = newScreen.getScreenshotAs(OutputType.BYTES);
        return scnShot;
    }



}

