# Hudl Quality Engineering Technical Project

This is the Hudl Quality Engineering Technical Project by Ralph Gehy

This goes through the Quality Assurance analysis and the Automated Testing of the Hudl login functionality. 

This Google Sheet provides the Test Scenarios and Test Cases from the QA analysis:
https://docs.google.com/spreadsheets/d/1pAeV4pOy4RaVZZmNE2ZzDQImL3hLiPaHuIKHyf0JZXM/edit?usp=sharing

This Google Doc is the documentation of a potential issue I encountered after logging in successfully.
https://docs.google.com/document/d/1jk5yXJC_VRb99CpZV9LKvB235SKxkAM3rrAH9s72jhY/edit?usp=sharing

The directory break down below displays the file structure that the framework and tests were built.
<img width="497" alt="image" src="https://user-images.githubusercontent.com/19873511/213957083-7d5a1ad6-44af-4789-a64e-fba1e6037f47.png">

## Software Libraries Used
This project was created with Java, Selenium WebDriver, Maven, TestNG, Extent Reports, WebDriverManager

## Framework

Generally, the framework package will be developed, maintained, and updated in a separate library and be imported into any automation project that will provide consistency across automated testing projects.
The framework contains setting up the Selenium WebDriver, the ability to execute the tests in Google Chrome, Mozilla Firefox, Safari, Internet Explorer and Microsoft Edge. 
The framework also uses WebDriverManager to simplify the use of manually maintaining browser driver binaries.

The framework also enhances the assertions to document what is being verified and takes a screenshot which is added to the report. In addition, through the WebDriverActions class, it simplifies common methods which makes it faster and easier to automate the actual test cases.

Lastly, the framework integrates ExtentReports with a custom listener that will write the results with screenshots on Assertions as well as screenshots on failures.

The current version of the framework is mainly focused on UI End to End testing but can add the capabilities of backend API testing as well as database testing. In addition, the framework can be enhanced to run headless, run on a VNC based Selenium Grid either locally or in a Cloud server.


## Test Cases
The test cases that were automated can be see in the Google Sheets provided above. The automation strategy with the use of the Page Object Model (POM) and Page Factories. In the pages package, it has the classes for the different pages, the selectors, as well as the actions.

In the actual LoginTests class, it creates an instance of the Page objects being used (Home page, Login page, Authenticated Home page) to navigate through the site. This reduces the amount of repitive code when automating which leads to greater efficiency.

## Executing the Test Cases
This automation project can be executed through a Java-based IDE or through the command-line with Maven. Maven must be installed in order to successfully be executed. When executing, be sure to pass valid credentials through the VM options or the command-line -Dusername=YourEmailGoesHere -Dpassword=YourPasswordGoesHere

The code to execute in the command line will be the following:

`mvn test -DsuiteXmlFile="testng-xml-runners/hudl-testng-runner.xml" -Dusername="ValidEmailAddressGoesHere" -Dpassword="ValidPasswordGoesHere"`

## Reporting 
Below is a sample report generated from the test execution. You can see the HTML generated report here: test-output/reports/2023-01-22/report_2023-01-22_175328.html
<img width="1419" alt="image" src="https://user-images.githubusercontent.com/19873511/213948337-6dad1436-100b-458e-8fea-8bba803ffec5.png">


## Conclusion
Thank you for taking the time to review my Hudl Technical Project. While this project was written in Java, while getting familiar with Selenium for Python, many of the features of this framework can be implemented in a Python Selenium framework. The concepts for the page object model when automating the test cases are the same in Python as they are in Java.


## *** Update ***
1. Refactored how the drivers are created in order to allow parallel execution while still cleaning maintaining the use of the Page Object Model as well as the highly customized Extent Report. 
2. Integrated the ability to run the tests either on the local machine or on a Selenium Grid environment by passing the parameters of either LOCAL or GRID to in the TestNG XML file as well as the Grid URL. Therefore, you can either run on a local grid, grid hosted on internal server behind VPN restricted access, or a SAAS enterprise grid provider such as AWS Device Farm, Browser Stack, and/or SauceLabs.

### Extent Report Updates
<img width="1433" alt="image" src="https://user-images.githubusercontent.com/19873511/215299249-61e3d149-93ed-4d9a-b829-05d2ae8bdd42.png">

![image](https://user-images.githubusercontent.com/19873511/215299430-bf525166-3c66-4558-a0ab-2f0ddd71b9f8.png)
