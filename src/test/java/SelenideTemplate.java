/*
This class is a template to demonstrate the structure of a test class that
- uses TestNG and Selenide.
- takes a snapshot on failure (target is 'build/reports/tests'),
    automatically through Selenide

Other Selenide facts:
- $.should* methods wait up to 4 seconds by default

 */

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class SelenideTemplate extends BaseSelenide {



    @BeforeClass
    public void Initialize() {
        super.Setup();
    }

    @Test
    public void AmazonHoverTest() {
        open("https://www.amazon.com/");
        $(By.id("nav-link-accountList")).hover();
        $(By.linkText("Start here.")).shouldBe(Condition.visible);
    }

    @Test
    public void SimpleGoogleSearch(){
        open("https://www.google.com");
        $(By.name("q")).setValue("Paris France").pressEnter();
        /* hover over each method in the line below:
         assertTrue()   is from TestNG
         getWebDriver() is from Selenide
         getTitle()     is from Selenium
         equals()       is from Java
         */
        Assert.assertTrue(getWebDriver().getTitle().equals("Paris France - Google Search"));
    }

    // advanced google search for phrase using the French language
    // verify "Search French pages" element appears on the results page
    // uncomment sleeps to observe results
    @Test
    public void AdvancedGoogleSearch() {
        open("https://www.google.com/advanced_search");
        $(byName("as_q")).val("bon appetit");
        $("#lr_button").click();
        $(withText("French")).click();
        //Selenide.sleep(3000);
        $(byValue("Advanced Search")).click();

        $(byText("Search French pages")).shouldBe(visible);
        //Selenide.sleep(5000);
    }

    /*
    not working the way I want yet, will come back to this...
    @Test
    public void AmazonIphoneBestSeller(){
        open("https://amazon.com");
        $(By.name("field-keywords")).val("iPhone").pressEnter();

        $$(byClassName("s-main-slot")).shouldHaveSize(1);

        // find closest (first) result - should have a visible image {doesn't work}
        //$(byClassName("s-main-slot")).closest("img").shouldBe(visible);

        Selenide.sleep(4000);
    }*/

    @AfterClass
    public void tearDown() {
        if (driver != null)
            driver.quit();
    }
}
