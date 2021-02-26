
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

import org.openqa.selenium.*;
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.openqa.selenium.support.locators.RelativeLocator.withTagName;


public class TestTemplate extends BaseTest{

    String environment;

    // these parameters are defined in the TestNG 'suite' files, but are all optional
    // if not defined, the default values are 'Dev', 'false' and 'true'
    @Parameters({"Environment", "Grid", "Incognito"})
    @BeforeMethod
    public void Setup(@Optional("Dev")String env, @Optional("false") boolean runOnGrid,
                      @Optional("true") boolean incognito) throws IOException {
        super.InitializeTest(env, runOnGrid, incognito);
        environment = env;
    }


    /*
    Go to MacRumors site, show full contents of first article and
    print out the date/time it was published in the console.
     */
    @Test
    public void MacRumors() throws InterruptedException {
        WebDriver driver = getDriver();
        driver.get("https://www.macrumors.com/");
        // window size is set in BaseTest but we can also specify where the window is displayed
        driver.manage().window().setPosition(new Point(1000, 100));

        // new features of Selenium4, "withTagName" and ".below"
        // find the "next" anchor tag, which is to the right of "Archives" and click it
        WebElement firstArticle = driver.findElement(withTagName("article"));
        WebElement showFullArticle = firstArticle.findElement(By.partialLinkText("Show Full Article"));
        WebElement timePublished = driver.findElement(withTagName("time").below(firstArticle));

        String timeString = timePublished.getText();
        System.out.println("The most recent article was published: ");
        System.out.println(timeString);
        Thread.sleep(1000);

        // scroll the link into view
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,800)");
        Thread.sleep(5000);

        // expand to show the full article in-line
        showFullArticle.click();
        Thread.sleep(3000);
    }

    @Test
    public void GoogleNews() throws InterruptedException {
        WebDriver driver = getDriver();
        driver.get("https://www.news.google.com/");
        Thread.sleep(1000);
        Assert.assertEquals(driver.getTitle(), "The Google News");
    }

    @Test
    public void AdvancedGoogleSearch() throws InterruptedException {
        WebDriver driver = getDriver();
        // if env is dev, we'll go to the standard Google search page
        // if env is test, we'll go to the advanced Google search page
        driver.get(super.environmentConfig.getProperty("url"));
        Thread.sleep(3000);
        String locator = "as_q";
        if(environment.equalsIgnoreCase("dev"))
            locator = "q";
        WebElement queryInput = driver.findElement(By.name(locator));
        queryInput.sendKeys("kittens");
        queryInput.sendKeys(Keys.ENTER);
        Thread.sleep(5000);
    }

    /*
    Go to a site, simulate a user opening several tabs in the current browser.
    Close each tab separately.
     */
    @Test
    public void  CNBC() throws InterruptedException {
        WebDriver driver = getDriver();
        driver.get("https://cnbc.com");
        String thisWindow = driver.getWindowHandle();
        Thread.sleep(1000);

        // simulate user opening 2 more tabs
        ((JavascriptExecutor)driver).executeScript("window.open()");
        ((JavascriptExecutor)driver).executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        driver.get("https://www.google.com/");
        driver.switchTo().window(tabs.get(2));
        driver.get("https://www.google.com/");
        Thread.sleep(2000);

        // cycle through the tabs, closing each one but the original
        for(String nextTab : driver.getWindowHandles()) {
            driver.switchTo().window(nextTab);
            if(!thisWindow.equalsIgnoreCase(nextTab)) {
                driver.close();
                Thread.sleep(2000);
            }
        }
        Thread.sleep(5000);
    }
    @Test
    public void  CNET() throws InterruptedException {
        WebDriver driver = getDriver();
        driver.get("https://cnet.com");
        Thread.sleep(1000);
    }
    @Test
    public void TomsHardware() throws InterruptedException {
        WebDriver driver = getDriver();
        driver.get("https://tomshardware.com");
        Thread.sleep(1000);
    }
    @Test
    public void  SeleniumHome() throws InterruptedException {
        WebDriver driver = getDriver();
        driver.get("http://seleniumhq.org");
        Thread.sleep(1000);
    }
    @Test
    public void  JetBrainsBlog() throws InterruptedException {
        WebDriver driver = getDriver();
        driver.get("https://blog.jetbrains.com/blog/category/new-products/");
        Thread.sleep(1000);
    }
    @Test
    public void  VisualStudioDevCommunity() throws InterruptedException {
        WebDriver driver = getDriver();
        driver.get("https://developercommunity.visualstudio.com/spaces/21/index.html");
        Thread.sleep(1000);
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
