import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class Lesson7BPDnDTests {
    SelenideElement firstCard = $x("//*[@id='card-65']");
    SelenideElement cartButton = $x("//*[@id = 'open-cart-btn']");

    @BeforeEach
    void setUp() {
        System.out.println("Test method start\n" +
                "=================================================\n" +
                "Запуск браузера и открытие сайта\n");
        open("http://localhost:8080/");
    }

    @Test
    @Tag("UI")
    void simpleDND(){
        sleep(1000);
        firstCard.dragAndDrop(DragAndDropOptions.to(cartButton));
        sleep(2000);
    }


    @Test
    @Tag("UI")
    void anotherDND(){
        actions().moveToElement(firstCard)
                .clickAndHold()
                .pause(1000)
                .moveToElement(cartButton)
                .release()
                .pause(Duration.ofSeconds(2));
    }


    @Test
    @Tag("UI")
    void slowDND(){
        int firstCardX = firstCard.getCoordinates().inViewPort().getX() + firstCard.getSize().getWidth()/2;
        int firstCardY = firstCard.getCoordinates().inViewPort().getY() + firstCard.getSize().getHeight()/2;
        int cartButtonX= cartButton.getCoordinates().inViewPort().getX() + cartButton.getSize().getWidth()/2;;
        int cartButtonY = cartButton.getCoordinates().inViewPort().getY() + cartButton.getSize().getHeight()/2;;

    int diffX = cartButtonX - firstCardX;
    int diffxY = cartButtonY - firstCardY;

    sleep(1000);
    Actions dndActions = actions().moveToElement(firstCard).clickAndHold();
    actions().moveToElement(firstCard).clickAndHold().perform();
        for (int i = 0; i<50; i++) {
            dndActions.moveByOffset(diffX/50, diffxY/50);
        }
        dndActions.release().perform();
        sleep(2000);
    }
}
