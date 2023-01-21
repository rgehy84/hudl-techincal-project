package framework;

import com.aventstack.extentreports.Status;
import org.testng.Reporter;

public class BaseUtilities {

    /***
     * simplified printing of the message to output since most statuses on the extent report will INFO.
     * For pass or fail, use the method with the status parameter
     * @param textToOutput => text that will be logged in reports
     */
    public void print(String textToOutput) {
        print(textToOutput, Status.INFO);
    }

    /***
     * this method is used in order to output the to the console, TestNG reporting, and ExtentReport. It prevents
     * having to use system output
     * @param textToOutput => text that will be logged in reports
     * @param status => message type displayed in the extent report
     */
    public void print(String textToOutput, Status status) {
        Reporter.log(textToOutput, true);
    }
}
