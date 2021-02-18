
/*
This class is a template to demonstrate the organization of new test classes that
- can use parallelization
- takes a snapshot on failure
- logs any console errors

To execute:
1. Right-click in a Test to run just that test
2. Right-click outside of a Test to run all tests in this class
3. Right-click the runner file "DevSuite_Parallel.xml" to run all the tests in this class.
    This is the only method that will execute the tests in parallel (since parallelization
    is specified in the suite).
 */

import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;

public class TestTemplate extends BaseTest{

    @Parameters({"Environment", "Grid"})
    @BeforeMethod
    public void Setup(@Optional("Dev")String env, @Optional("False") String runOnGrid) throws IOException {
        super.InitializeTest(env, runOnGrid, false);
    }


    // typically a Test will specify the User Profile.xlsx "sheet name" being used
    //@Parameters({"user-profile-sheet-name"})
    @Test
    public void MacRumors() throws InterruptedException {
        WebDriver driver = getDriver();
        driver.get("https://www.macrumors.com/");
        // just for fun
        driver.manage().window().setPosition(new Point(1000, 100));
        Thread.sleep(300);
    }

    @Test
    public void GoogleNews() throws InterruptedException {
        WebDriver driver = getDriver();
        driver.get("https://www.news.google.com/");
        Thread.sleep(300);
        Assert.assertEquals(driver.getTitle(), "The Google News");
    }

    @Test
    public void  CNBC() throws InterruptedException {
        WebDriver driver = getDriver();
        driver.get("https://cnbc.com");
        Thread.sleep(300);
    }
    @Test
    public void  CNET() throws InterruptedException {
        WebDriver driver = getDriver();
        driver.get("https://cnet.com");
        Thread.sleep(300);
    }
    @Test
    public void TomsHardware() throws InterruptedException {
        WebDriver driver = getDriver();
        driver.get("https://tomshardware.com");
        Thread.sleep(300);
    }
    @Test
    public void  SeleniumHome() throws InterruptedException {
        WebDriver driver = getDriver();
        driver.get("http://seleniumhq.org");
        Thread.sleep(300);
    }
    @Test
    public void  JetBrainsBlog() throws InterruptedException {
        WebDriver driver = getDriver();
        driver.get("https://blog.jetbrains.com/blog/category/new-products/");
        Thread.sleep(300);
    }
    @Test
    public void  VisualStudioDevCommunity() throws InterruptedException {
        WebDriver driver = getDriver();
        driver.get("https://developercommunity.visualstudio.com/spaces/21/index.html");
        Thread.sleep(300);
    }

    // Run even if one or more methods failed or was skipped
    @AfterMethod(alwaysRun = true)
    public void Teardown(ITestResult testResult){
        WebDriver driver = getDriver();
        if(testResult.getStatus() == ITestResult.FAILURE){
            try{
                TakesScreenshot ts = (TakesScreenshot)driver;
                File source = ts.getScreenshotAs(OutputType.FILE);
                FileHandler.copy(source, new File("./Screenshots/" +
                        testResult.getName() + ".png"));
            }catch(Exception e){
                String msg = e.getMessage();
            }
        }
        driver.quit();
        drivers.remove();
    }

}
