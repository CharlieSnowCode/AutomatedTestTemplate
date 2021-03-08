import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.codeborne.selenide.WebDriverRunner.setWebDriver;

public class BaseSelenide {

    protected WebDriver driver;
    protected ChromeOptions chromeOptions;


    public void Setup(){
        System.setProperty("webdriver.chrome.driver",
                (System.getProperty("user.dir") + "\\src\\main\\resources\\chromedriver.exe"));
        chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("window-size=1200,900");
        driver = new ChromeDriver(chromeOptions);
        setWebDriver(driver);

    }
}
