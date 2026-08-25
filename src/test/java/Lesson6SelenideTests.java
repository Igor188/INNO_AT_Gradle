import com.codeborne.selenide.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Lesson6SelenideTests {
    WebDriver driver;

    @BeforeEach
    void setup(){
        Selenide.open("http://localhost:8080/");
//        Configuration.timeout = 10000;
    }


    @Test
    @Tag("UI")
    @Order(1)
    @Disabled
    void selenideTest(){
        //SelenideElement cartButton = $("#open-cart-btn"); CSS - Локатор

        //xpath - локатор
        SelenideElement cartButton = $x("//*[@id='open-cart-btn']");
        cartButton.click();
        sleep(5000);
        //cartButton.click();
    }

    @Test
    @Tag("UI")
    @Order(2)
    @Disabled
    void selenideMethods(){
        ElementsCollection cardTitleList = $$x("//*[contains(@id,card)]/h4");
        cardTitleList.forEach(cardTitle -> {
            System.out.println(cardTitle.text());
        });
    }

    @Test
    @Tag("UI")
    @Order(3)
    @Disabled
    void selenideAsserts(){
        SelenideElement cartButton = $x("//*[@id='open-cart-btn']");
        cartButton.should(Condition.visible, Duration.ofSeconds(10));
        cartButton.click();
        cartButton.should(Condition.interactable, Duration.ofSeconds(1));
        ElementsCollection cardTitleList = $$x("//*[contains(@id,card)]/h4");
        cardTitleList.forEach(cardTitle -> {
            System.out.println(cardTitle.text());
        });

    }

    @Test
    @Tag("UI")
    @Order(4)
    void JSAlert(){
        SelenideElement addToCartButton = $x("//*[@data-action = 'add-to-cart']");
        addToCartButton.click();
        addToCartButton.click();
        addToCartButton.click();
        addToCartButton.click();
        $("#open-cart-btn").click();
        $("#makeOrder").click();
        sleep(1000);
        Alert activeAlert = Selenide.switchTo().alert();
        System.out.println(activeAlert.getText());
        sleep(3000);
        activeAlert.accept();
        sleep(1000);
    }



}
