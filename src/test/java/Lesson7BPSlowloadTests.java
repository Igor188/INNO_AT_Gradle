import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class Lesson7BPSlowloadTests {

    @BeforeEach
    void setUp() {
        System.out.println("Test method start\n" +
                "=================================================\n" +
                "Запуск браузера и открытие сайта\n");
        open();
        sleep(30000);
        Configuration.pageLoadTimeout = 5000;
        open("http://localhost:8080/");
    }

    @Test
    @Tag("UI")
    void hiddenTest(){
        SelenideElement addToCartButton = $x("//*[@data-action='add-to-cart']");
        addToCartButton.click();
        addToCartButton.click();
        addToCartButton.click();

        SelenideElement notificationContainer = $x("(//*[@datatest='notification-container'])[1]");
        SelenideElement secondNotificationContainer = $x("(//*[@datatest='notification-container'])[2]");
        SelenideElement thirdNotificationContainer = $x("(//*[@datatest='notification-container'])[3]");
        notificationContainer.shouldNot(visible);
        notificationContainer.should(exist);
        notificationContainer.$x("./..").should(visible);

        secondNotificationContainer.shouldNot(visible);
        secondNotificationContainer.should(exist);
        secondNotificationContainer.$x("./..").should(visible);

        thirdNotificationContainer.shouldNot(visible);
        thirdNotificationContainer.should(exist);
        thirdNotificationContainer.$x("./..").should(visible);
    }



}
