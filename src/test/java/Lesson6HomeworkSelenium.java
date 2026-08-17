import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Random;
import java.util.UUID;

import static javax.swing.text.html.CSS.getAttribute;
import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Lesson6HomeworkSelenium {

    private final Random random = new Random();

    // Сохраняем данные созданного товара и цены для использования в ассертах
    private static String createdGoodName;
    private static Double createdGoodPrice;

    //1.5. Инициализацию браузера и его закрытие необходимо вынести в отдельные методы для выполнения перед и после тестов.

    WebDriver driver;

    @BeforeEach
    void setup(){
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
    }

    @AfterEach
    void tearDown(){
        driver.quit();
    }


//1.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается.
    @Test
    @Tag("UI")
    @Order(1)
    void AddGoodAndCheckTest(){

        //вход в админку
        driver.findElement(By.cssSelector("[href='/admin']")).click();
        driver.findElement(By.id("username")).click();
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).sendKeys("secret123");
        driver.findElement(By.xpath("//*[@type='submit']")).click();

        // Создаём товар с рандомными значениями имени и цены
        String name = "Good-" + UUID.randomUUID().toString().substring(0, 8);
        Double price = random.nextDouble(1.0, 100.0);

        //Добавление товара через админку
        driver.findElement(By.id("n-name")).click();
        driver.findElement(By.id("n-name")).sendKeys(name);
        driver.findElement(By.id("n-price")).click();
        driver.findElement(By.id("n-price")).sendKeys(String.valueOf(price));
        driver.findElement(By.id("add-btn")).click();

        // Сохраняем данные созданного товара и цены для использования в ассертах
        createdGoodName = name;
        createdGoodPrice = price;

        //Проверка тоста успешного добавления товара в админке
        String toastText = driver.findElement(By.xpath("//*[@class = 'toast']")).getText();
        assertThat(toastText)
                .as("Проверка тоста успешного добавления товара")
                .isEqualTo("Товар успешно добавлен!");

        //выход из админки
        driver.findElement(By.xpath("//*[@href= '/']")).click();

        //Проверка добавления товара по имени
        assertThat(driver.findElement(By.cssSelector(".product-card h4")).getAttribute("value"))
                .as("Добавленное имя должно быть в списке имен товаров")//сравнение добавленного имени
                .isEqualTo(name);

        //Проверка добавления товара по цене
        assertThat(driver.findElement(By.cssSelector(".product-card [style*=\"font-weight:bold\"]")).getAttribute("value"))
                .as("Добавленная цена должна быть в списке цен товаров")//сравнение добавленного имени
                .isEqualTo(price);


    }







}




/*

Задача 1: написать 4 автотеста с использованием фреймворка Selenium по следующим кейсам:

1.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается.

1.2. Добавить товар в корзину и проверить, что он отображается.

1.3. Попытаться войти в админку с неверным логином и паролем.

1.4. Проверить сохранение товаров в корзине после обновления страницы.

1.5. Инициализацию браузера и его закрытие необходимо вынести в отдельные методы для выполнения перед и после тестов.


Критерии проверки

Задача 1:

1.1–1.4 * (обязательные) В проекте имеется метод, автоматизирующий тестовый кейс — 3 б. / каждый

1.5 Запуск браузера и его закрытие вынесены в отдельные методы для выполнения перед и после тестового метода — 3 б.

 */