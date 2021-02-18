
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    // thread-safe collection of WebDrivers
    protected static final ThreadLocal<WebDriver> drivers = new ThreadLocal<>();
    protected static Properties environmentConfig;

    protected WebDriver getDriver(){
        return drivers.get();
    }

    protected void InitializeTest(String env, String useGrid, boolean incognito) throws IOException {
        // initialize file for environment-based settings
        environmentConfig = new Properties();
        FileInputStream environmentConfigFile = null;
        if(env.equalsIgnoreCase("Test")){
            environmentConfigFile = new FileInputStream(System.getProperty("user.dir") +
                    "\\src\\main\\resources\\TestEnv.properties");
        } else {
            environmentConfigFile = new FileInputStream(System.getProperty("user.dir") +
                    "\\src\\main\\resources\\DevEnv.properties");
        }
        environmentConfig.load(environmentConfigFile);
        String browserName = environmentConfig.getProperty("browser");
        String gridNodeUrl = environmentConfig.getProperty("gridNodeUrl");

        // currently only supporting Chrome
        System.setProperty("webdriver.chrome.driver",
                (System.getProperty("user.dir") + "\\src\\main\\resources\\chromedriver.exe"));
        if(useGrid.equalsIgnoreCase("True")){
            DesiredCapabilities capability = new DesiredCapabilities();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("window-size=1680,1050");
            RemoteWebDriver remoteWebDriver = new RemoteWebDriver(new URL(gridNodeUrl), options);
            remoteWebDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            drivers.set(remoteWebDriver);
            capability.setBrowserName(browserName);
            capability.setPlatform(Platform.XP);
        } else {
            ChromeOptions options = new ChromeOptions();
            if(incognito)
                options.addArguments("--incognito");
            //options.addArguments("--no-sandbox");// not sure about this one, why NOT run in sandbox?
            options.addArguments("window-size=1680,1050");
            WebDriver webDriver = new ChromeDriver(options);
            webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            drivers.set(webDriver);
        }

    }
}
