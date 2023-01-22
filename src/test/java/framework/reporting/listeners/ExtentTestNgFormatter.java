package framework.reporting.listeners;


import framework.reporting.extent.ExtentTestManager;
import framework.reporting.extent.SystemInfo;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.google.common.base.Preconditions;
import framework.BaseDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import org.testng.util.Strings;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * This class houses the listener for the TestNG which generates the html report by using Extent Report.
 */


public class ExtentTestNgFormatter extends ExtentTestManager implements ISuiteListener, ITestListener, IInvokedMethodListener, IReporter {
    private static final String REPORTER_ATTR = "extentTestNgReporter";
    private static ExtentReports reporter;
    private List<String> testRunnerOutput;
    private Map<String, String> systemInfo;
    private ExtentSparkReporter sparkReporter;

    private static ExtentTestNgFormatter instance;
    private BaseDriver baseDriver = new BaseDriver();

    public ExtentTestNgFormatter() {
        String getDate = baseDriver.timestamp("yyyy-MM-dd");
        String getTime = baseDriver.timestamp("HHmmss");

        setInstance(this);
        testRunnerOutput = new ArrayList<>();
        String reportPathStr = System.getProperty("reportPath");
        File reportPath;

        try {
            reportPath = new File(reportPathStr);
        } catch (NullPointerException e) {
            reportPath = new File(TestNG.DEFAULT_OUTPUTDIR + "/reports/" + getDate);
        }

        if (!reportPath.exists()) {
            if (!reportPath.mkdirs()) {
                throw new RuntimeException("Failed to create output run directory");
            }
        }

        File reportFile = new File(reportPath, "report_"+ getDate + "_" + getTime +".html");
        File emailReportFile = new File(reportPath, "emailable-report_" + getDate + "_" + getTime + ".html");

        sparkReporter = new ExtentSparkReporter(reportFile);
        reporter = new ExtentReports();
        reporter.attachReporter(sparkReporter);
    }

    /**
     * Gets the instance of the {@link ExtentTestNgFormatter}
     *
     * @return The instance of the {@link ExtentTestNgFormatter}
     */
    public static ExtentTestNgFormatter getInstance() {
        return instance;
    }

    private static void setInstance(ExtentTestNgFormatter formatter) {
        instance = formatter;
    }

    /**
     * Gets the system information map
     *
     * @return The system information map
     */
    public Map<String, String> getSystemInfo() {
        return systemInfo;
    }

    /**
     * Sets the system information
     *
     * @param systemInfo The system information map
     */
    public void setSystemInfo(Map<String, String> systemInfo) {
        this.systemInfo = systemInfo;
    }

    public void onStart(ISuite iSuite) {
        String configFile = iSuite.getParameter("report.config");

        if (!Strings.isNullOrEmpty(configFile)) {
            try {
                sparkReporter.loadXMLConfig(configFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        String systemInfoCustomImplName = iSuite.getParameter("system.info");
        if (!Strings.isNullOrEmpty(systemInfoCustomImplName)) {
            generateSystemInfo(systemInfoCustomImplName);
        }

        iSuite.setAttribute(REPORTER_ATTR, reporter);
    }

    private void generateSystemInfo(String systemInfoCustomImplName) {
        try {
            Class<?> systemInfoCustomImplClazz = Class.forName(systemInfoCustomImplName);
            if (!SystemInfo.class.isAssignableFrom(systemInfoCustomImplClazz)) {
                throw new IllegalArgumentException("The given system.info class name <" + systemInfoCustomImplName +
                        "> should implement the interface <" + SystemInfo.class.getName() + ">");
            }

            SystemInfo t = (SystemInfo) systemInfoCustomImplClazz.newInstance();
            setSystemInfo(t.getSystemInfo());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public void onFinish(ISuite iSuite) {
    }


    public void onTestStart(ITestResult iTestResult) {
        String testName = iTestResult.getInstance().getClass().getSimpleName() + " \u00BB " +
                iTestResult.getMethod().getMethodName();

        ExtentTest test = reporter.createTest(testName);
        iTestResult.setAttribute("test", test);
    }

    public void onTestSuccess(ITestResult iTestResult) {
        ExtentTest test = (ExtentTest) iTestResult.getAttribute("test");
        test.pass("<span style=\"color:green;font-weight:bold;\">TEST PASSED: " + getTestMethodName(iTestResult) +
                " has passed");
    }

    private String base64conversion(WebDriver driver) {
        TakesScreenshot newScreen = (TakesScreenshot) driver;
        String scnShot = newScreen.getScreenshotAs(OutputType.BASE64);
        return scnShot ;
    }

    public void onTestFailure(ITestResult iTestResult) {
        ExtentTest test = (ExtentTest) iTestResult.getAttribute("test");
        try {
            test.fail("<span style=\"color:red;font-weight:bold;\">TEST FAILURE: " + getTestMethodName(iTestResult) +
                    " has failed");
            test.addScreenCaptureFromBase64String(base64conversion(baseDriver.getDriver())).fail("SCREENSHOT",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64conversion(baseDriver.getDriver())).build());
            test.fail(iTestResult.getThrowable());
        } catch (NullPointerException e) {
            System.out.println("Failure prior to method under test");
            e.printStackTrace();
        }
    }

    public void onTestSkipped(ITestResult iTestResult) {
        ExtentTest test = (ExtentTest) iTestResult.getAttribute("test");
        try {
            test.skip("<span style=\"color:darkorange;font-weight:bold;\">SKIPPED TEST: " + getTestMethodName(iTestResult) +
                    " has been skipped</span>");
            test.skip(iTestResult.getThrowable());
        } catch (NullPointerException e) {
            System.out.println("Failure prior to method under test");
            e.printStackTrace();
        }
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    public void onStart(ITestContext iTestContext) {
    }

    public void onFinish(ITestContext iTestContext) {
        reporter.flush();
    }

    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
    }

    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        if (iInvokedMethod.isTestMethod()) {
            ExtentTest test = (ExtentTest) iTestResult.getAttribute("test");
            List<String> logs = Reporter.getOutput(iTestResult);
            for (String log : logs) {
                if(log.toLowerCase().contains("failed")) {
                    test.warning(log);
                } else if (log.toLowerCase().contains("passed")) {
                    test.pass(log);
                } else {
                    test.info(log);
                }
            }

            for (String group : iInvokedMethod.getTestMethod().getGroups()) {
                test.assignCategory(group);
            }
        }
    }

    /**
     * Adds a screen shot image file to the report. This method should be used only in the configuration method
     * and the {@link ITestResult} is the mandatory parameter
     *
     * @param iTestResult The {@link ITestResult} object
     * @param filePath    The image file path
     * @throws IOException {@link IOException}
     */
    public void addScreenCaptureFromPath(ITestResult iTestResult, String filePath) throws IOException {
        ExtentTest test = (ExtentTest) iTestResult.getAttribute("test");
        test.addScreenCaptureFromPath(filePath);
    }

    /**
     * Adds a screen shot image file to the report. This method should be used only in the
     * {@link org.testng.annotations.Test} annotated method
     *
     * @param filePath The image file path
     * @throws IOException {@link IOException}
     */
    public void addScreenCaptureFromPath(String filePath) throws IOException {
        ITestResult iTestResult = Reporter.getCurrentTestResult();
        Preconditions.checkState(iTestResult != null);
        ExtentTest test = (ExtentTest) iTestResult.getAttribute("test");
        test.addScreenCaptureFromPath(filePath);
    }

    /**
     * Sets the test runner output
     *
     * @param message The message to be logged
     */
    public void setTestRunnerOutput(String message) {
        testRunnerOutput.add(message);
    }

    public void generateReport(List<XmlSuite> list, List<ISuite> list1, String s) {
        reporter.setSystemInfo("Operating System", System.getProperty("os.name"));
        if (getSystemInfo() != null) {
            for (Map.Entry<String, String> entry : getSystemInfo().entrySet()) {
                reporter.setSystemInfo(entry.getKey(), entry.getValue());
            }
        }
        reporter.addTestRunnerOutput(testRunnerOutput);
    }


    /**
     * Adds the new node to the test with the given node name.
     *
     * @param nodeName The name of the node to be created
     */
    public void addNewNodeToTest(String nodeName) {
        //addNewNode("test", nodeName);
    }


    /**
     * Adds a new node to the suite with the given node name
     *
     * @param nodeName The name of the node to be created
     */
    public void addNewNodeToSuite(String nodeName) {
        //addNewNode(SUITE_ATTR, nodeName);
    }

    private void addNewNode(String parent, String nodeName) {
        ITestResult result = Reporter.getCurrentTestResult();
        Preconditions.checkState(result != null);
        //ExtentTest parentNode = (ExtentTest) result.getAttribute(parent);
        //ExtentTest childNode = parentNode.createNode(nodeName);
        //result.setAttribute(nodeName, childNode);
    }


    /**
     * Adds a info log message to the node
     *
     * @param logMessage The log message string
     * @param nodeName   The name of the node
     */
    public void addInfoLogToNode(String logMessage, String nodeName) {
        ITestResult result = Reporter.getCurrentTestResult();
        Preconditions.checkState(result != null);
        ExtentTest test = (ExtentTest) result.getAttribute(nodeName);
        test.info(logMessage);
    }

    /**
     * Marks the given node as failed
     *
     * @param nodeName The name of the node
     * @param t        The {@link Throwable} object
     */
    public void failTheNode(String nodeName, Throwable t) {
        ITestResult result = Reporter.getCurrentTestResult();
        Preconditions.checkState(result != null);
        ExtentTest test = (ExtentTest) result.getAttribute(nodeName);
        test.fail(t);
    }


    /**
     * Marks the given node as failed
     *
     * @param nodeName   The name of the node
     * @param logMessage The message to be logged
     */
    public void failTheNode(String nodeName, String logMessage) {
        ITestResult result = Reporter.getCurrentTestResult();
        Preconditions.checkState(result != null);
        ExtentTest test = (ExtentTest) result.getAttribute(nodeName);
        test.fail(logMessage);
    }

    private static String getTestMethodName(ITestResult iTestResult) {
        String getTestMethodName = null;
        try {
            getTestMethodName = iTestResult.getMethod().getConstructorOrMethod().getName();
        } catch (NullPointerException e) {
            getTestMethodName = "No Test Method Name: \n" +
                    "Test failed prior to executing the method under test";
        }
        return getTestMethodName;
    }
}

