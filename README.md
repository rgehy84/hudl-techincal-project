# hudl-techincal-project

This is the Hudl Technical Project by Ralph Gehy

This goes through the Quality Assurance analysis and the Automated Testing of the Hudl login functionality. 

This Google Sheet provides the Test Scenarios and Test Cases from the QA analysis:
https://docs.google.com/spreadsheets/d/1pAeV4pOy4RaVZZmNE2ZzDQImL3hLiPaHuIKHyf0JZXM/edit?usp=sharing

This Google Doc is the documentation of a potential issue I encountered after logging in successfully.
https://docs.google.com/document/d/1jk5yXJC_VRb99CpZV9LKvB235SKxkAM3rrAH9s72jhY/edit?usp=sharing

The directory break down below displays the file structure that the framework and tests were built.
<img width="339" alt="image" src="https://user-images.githubusercontent.com/19873511/213945905-c343f996-830e-45a7-b7c8-708ffd80aca4.png">

## Software Libraries Used
This project was created with Java, Selenium WebDriver, Maven, TestNG, Extent Reports, WebDriverManager

## Framework

Generally, the framework package will be developed, maintained, and updated in a separate library and be imported into any automation project that will provide consistency across automated testing projects.
The framework contains setting up the Selenium WebDriver, the ability to execute the tests in Google Chrome, Mozilla Firefox, Safari, Internet Explorer and Microsoft Edge. 
The framework also uses WebDriverManager to simplify the use of manually maintaining browser driver binaries.

The framework also enhances the assertions to document what is being verified and takes a screenshot which is added to the report. In addition, through the WebDriverActions class, it simplifies common methods which makes it faster and easier to automate the actual test cases.

Lastly, the framework integrates ExtentReports with a custom listener that will write the results with screenshots on Assertions and well as screenshots on failures.

The current version of the framework is mainly focused on UI End to End testing but can add the capabilities of backend API testing as well as database testing. 


## Test Cases
The test cases that were automated can be see in the Google Sheets provided above. The automation strategy with the use of the Page Object Model (POM) and Page Factories. In the pages package, it has the classes for the different pages, the selectors, as well as the actions.

In the actual LoginTests class, it creates an instance of the Page objects being used (Home page, Login page, Authenticated Home page) to navigate through the site. This reduces the amount of repitive code when automating which leads to greater efficiency.

## Executing the Test Cases
This automation project can be executed through a Java-based IDE or through the command-line with Maven. When executing, be sure to pass valid credentials through the VM options or the command-line -Dusername=YourEmailGoesHere -Dpassword=YourPasswordGoesHere
`code goes here`
