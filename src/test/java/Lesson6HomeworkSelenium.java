import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Random;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Lesson6HomeworkSelenium {

    private final Random random = new Random();

    // Сохраняем данные созданного товара и цены, количества, итоговой суммы корзины для использования в ассертах
    private static String createdGoodName;
    private static Double createdGoodPrice;
    private static Integer addGoodAmountInCart;
    private static double CartItemTotal;

    //1.5. Инициализацию браузера и его закрытие необходимо вынести в отдельные методы для выполнения перед и после тестов.

    WebDriver driver;

    @BeforeEach
    void setup(){
        System.out.println("Test method start\n" +
                           "=================================================\n" +
                           "Запуск браузера и открытие сайта\n");
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
    }


    @AfterEach
    void tearDown(){
        System.out.println("Test method end\n" +
                           "==================================\n" +
                           "Закрытие браузера и очистка памяти\n");
        driver.quit();
    }


//==========================================================================================================================================


//1.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается.
    @Test
    @Tag("UI")
    @Order(1)
    void AddGoodAndCheckTest(){
        System.out.println("\n=== Тест 1.1: Добавить товар через админку, выйти на витрину и проверить, что товар отображается ===\n");

        //вход в админку
        System.out.println("\n=== Этап входа в админку ===\n");
        System.out.println("[INFO] Переход на страницу админки");
        driver.findElement(By.cssSelector("[href='/admin']")).click();

        driver.findElement(By.id("username")).click();
        System.out.println("[INFO] Ввод валидного логина");
        driver.findElement(By.id("username")).sendKeys("admin");

        driver.findElement(By.id("password")).click();
        System.out.println("[INFO] Ввод валидного пароля");
        driver.findElement(By.id("password")).sendKeys("secret123");

        System.out.println("[INFO] Нажатие кнопки входа");
        driver.findElement(By.xpath("//*[@type='submit']")).click();

        System.out.println("\n=== Этап входа в админку (SUCCESS) ===\n");

        // Создаём товар с рандомными значениями имени и цены
        System.out.println("\n=== Этап создания товара с рандомными значениями имени и цены ===\n");
        String name = "Good-" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("[INFO] Генерация случайного имени: " + name);
        Double price = random.nextDouble(1.0, 100.0);
        System.out.println("[INFO] Генерация случайной цены: " + price);

        System.out.println("\n=== Этап создания товара с рандомными значениями имени и цены (SUCCESS) ===\n");
        // Сохраняем данные созданного товара и цены для использования в ассертах в других методах
        createdGoodName = name;
        createdGoodPrice = price;

        //Добавление товара через админку
        System.out.println("\n=== Этап добавления товара через админку ===\n");

        driver.findElement(By.id("n-name")).click();
        System.out.println("[INFO] Ввод имени товара: " + name);
        driver.findElement(By.id("n-name")).sendKeys(name);

        driver.findElement(By.id("n-price")).click();
        System.out.println("[INFO] Ввод цены товара: " + price);
        driver.findElement(By.id("n-price")).sendKeys(String.valueOf(price));

        System.out.println("[INFO] Нажатие кнопки 'Создать'");
        driver.findElement(By.id("add-btn")).click();


        // проверка тоста об успешном добавлении товара (опционально, захотелось для полноты покрытия)
        System.out.println("[INFO] Ожидание появления тоста об успешном создании товара...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.id("toast-container"), "Товар успешно добавлен!"));
        String toastText = driver.findElement(By.id("toast-container")).getText();
        assertThat(toastText)
                .as("Проверка тоста об успешном добавлении товара")
                .isEqualTo("Товар успешно добавлен!");
        System.out.println("[INFO] Проверка тоста об успешном создании товара. Тост соответствует");

        System.out.println("\n=== Этап добавления товара через админку (SUCCESS) ===\n");

        //выход из админки
        System.out.println("[INFO] Выход из админки - нажать вернуться на сайт");
        driver.findElement(By.xpath("//*[@href= '/']")).click();
        System.out.println("\n=== Выход из админки (SUCCESS) ===\n");

        // находим карточку именно нашего товара по data-name
        System.out.println("\n=== Этап поиска товара и выполнения проверок ===\n");
        System.out.println("[INFO] Поиск карточки товара по data-name: " + name);
        //Пришлось делать ожидание 2 сек - иногда фейлится
        WebDriverWait waiting = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement productCard = waiting.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[contains(@class, 'product-card') and @data-name='" + name + "']")
                )
        );
        System.out.println("[INFO] Карточка успешно найдена");

        // проверка имени
        System.out.println("[INFO] Поиск и проверка имени: " + name + " в карточке товара");
        WebElement productName = productCard.findElement(By.tagName("h4"));
        assertThat(productName.getText())
                .as("Добавленное/созданное имя " + name + " должно быть в карточке товара", name)
                .isEqualTo(name);
        System.out.println("[INFO] Имя соответствует: " + name);

        // проверка цены
        System.out.println("[INFO] Поиск и проверка цены: " + price + " в карточке товара");
        WebElement productPrice = productCard.findElement(By.cssSelector("div:not([class])"));
        assertThat(productPrice.getText())
                .as(("Добавленная/созданная цена должна быть в карточке товара: " +price) , price)
                .contains(String.valueOf(price));
        System.out.println("[INFO] Цена соответствует: " + price);

        System.out.println("\n=== Этап поиска товара и выполнения проверок (SUCCESS) ===\n");

        System.out.println("\n=== Тест 1.1 завершён успешно ===\n");

     }

//==========================================================================================================================================


    //1.2. Добавить товар в корзину и проверить, что он отображается.
    @Test
    @Tag("UI")
    @Order(2)
    void AddGoodIntoCartCheckTest() {
        System.out.println("\n=== Тест 1.2: Добавить товар в корзину и проверить, что он отображается ===\n");

        // Создаём рандомное значение количества товаров/нажатий на кнопку в корзину в карточке товара
        System.out.println("\n=== Этап создания количества товара/нажатий в корзину с рандомными значениями ===\n");

        Integer amount = random.nextInt(1, 5);
        System.out.println("[INFO] Генерация количества нажатий на кнопку: " + amount);

        // Сохраняем данные созданного товара и цены для использования в ассертах в других методах
        addGoodAmountInCart = amount;

        System.out.println("\n=== Этап создания количества товара/нажатий в корзину с рандомными значениями (SUCCESS) ===\n");

        //Добавление созданного в тесте 1.1 товара в корзину
        System.out.println("\n=== Этап поиска ранее созданного в тесте 1.1 товара: " + createdGoodName + " и добавление его в корзину ===\n");


        // Явное ожидание появления кнопки "В корзину" (до 2 секунд)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement addButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@data-action='add-to-cart'][@data-name='" + createdGoodName + "']")
                )
        );
        System.out.println("[INFO] Кнопка 'В корзину' найдена и доступна");

        // Формируем цикл/количество нажатий + находим по сочетанию имени ранее созданного товара и data-action, добавляем в корзину товар
        for (int i = 1; i <= amount; i++) {
            addButton.click();
            System.out.println("[INFO] Товар найден. Нажимаем кнопку 'В корзину' для товара: " + createdGoodName);
            System.out.println("[INFO] Нажатие " + i + " выполнено");
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

            System.out.println("[INFO] Открываем корзину всех товаров");
            driver.findElement(By.id("open-cart-btn")).click();
            System.out.println("[INFO] Корзина открыта");


            System.out.println("\n=== Этап поиска ранее созданного в тесте 1.1 товара: " + createdGoodName + " и добавление его в корзину (SUCCESS) ===\n");


            // Ищем карточку товара в корзине по ранее созданному имени добавленного товара
            System.out.println("\n=== Этап поиска товара в корзине и выполнения проверок ===\n");
            System.out.println("[INFO] Поиск карточки товара по ранее созданному имени товара: " + createdGoodName);
            WebElement cartItem = driver.findElement(
                    By.xpath("//div[contains(@class, 'cart-item') and .//b[text()='" + createdGoodName + "']]")
            );
            System.out.println("[INFO] Карточка товара успешно найдена");

            // Проверка наименования добавленного товара = наименованию ранее созданного товара
            System.out.println("[INFO] Поиск и проверка имени: " + createdGoodName + " в карточке товара");
            WebElement itemName = cartItem.findElement(By.cssSelector("b"));
            assertThat(itemName.getText())
                    .as(("Созданное имя должно отображаться в карточке товара в корзине: " + createdGoodName), createdGoodName)
                    .isEqualTo(createdGoodName);
            System.out.println("[INFO] Имя соответствует: " + createdGoodName);


            // Проверка количества (должно соответстовать сгенерированном значению amount)
            System.out.println("[INFO] Поиск и проверка количества в карточке товара: ожидаемое " + amount);
            WebElement itemQty = cartItem.findElement(By.cssSelector(".qty-controls span"));
            assertThat(itemQty.getText())
                    .as(("Количество товара в корзине должно быть = " + amount), amount)
                    .isEqualTo(String.valueOf(amount));
            System.out.println("[INFO] Количество соответствует: " + amount);

            // Проверка суммы добавленного товара = цена ранее созданного товара * количество
            System.out.println("[INFO] Поиск и проверка суммы товара в карточке товара: ожидаемая сумма = " + (createdGoodPrice * amount));
            WebElement itemPrice = cartItem.findElement(By.cssSelector("div:nth-child(3)"));
            double expectedCartItemTotal = createdGoodPrice * amount;
            assertThat(itemPrice.getText())
                    .as(("Сумма товара должна в корзине должна быть: " + expectedCartItemTotal), expectedCartItemTotal)
                    .contains(String.valueOf(expectedCartItemTotal));
            System.out.println("[INFO] Сумма соответствует: " + expectedCartItemTotal);

            // Сохраняем данные созданной суммы корзины для использования в ассертах в других методах
            CartItemTotal = expectedCartItemTotal;

            System.out.println("\n=== Этап поиска товара в корзине и выполнения проверок (SUCCESS) ===\n");

            System.out.println("\n=== Тест 1.2 завершён успешно ===\n");

        }


//==========================================================================================================================================


    //1.3. Попытаться войти в админку с неверным логином и паролем.
    @Test
    @Tag("UI")
    @Order(3)
    void TryLog_inAdmin_panelIncorrectCredTest() {
        System.out.println("\n=== Тест 1.3: Попытаться войти в админку с неверным логином и паролем ===\n");

        //вход в админку
        System.out.println("\n=== Этап входа в админку (негативный тест) ===\n");
        System.out.println("[INFO] Переход на страницу админки");
        driver.findElement(By.cssSelector("[href='/admin']")).click();

        driver.findElement(By.id("username")).click();
        System.out.println("[INFO] Ввод невалидного логина" );
        driver.findElement(By.id("username")).sendKeys("admin25");

        driver.findElement(By.id("password")).click();
        System.out.println("[INFO] Ввод невалидного пароля");
        driver.findElement(By.id("password")).sendKeys("secret12345");

        System.out.println("[INFO] Нажатие кнопки входа");
        driver.findElement(By.xpath("//*[@type='submit']")).click();

        System.out.println("\n=== Этап входа в админку (негативный тест) (SUCCESS) ===\n");


        System.out.println("\n=== Этап проверок обработки невалидных введенных кредов в форму аутентификации (негативный тест) ===\n");
    // проверка тоста о введении невалидных кредах пользователя (опционально, захотелось для полноты покрытия)
        System.out.println("[INFO] Ожидание появления тоста о введении невалидных кредах пользователя...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.xpath("//*[@role = 'alert']"), "Неверные учетные данные пользователя"));
        String toastText = driver.findElement(By.xpath("//*[@role = 'alert']")).getText();
        assertThat(toastText)
                .as("Проверка тоста о введении невалидных кредах пользователя")
                .isEqualTo("Неверные учетные данные пользователя");
        System.out.println("[INFO] Проверка тоста о введении невалидных кредах пользователя. Тост соответствует");


    // проверка что мы остались на той же форме аутентификации
        System.out.println("[INFO] Проверка отображения формы аутентификации");
        assertThat(driver.findElement(By.cssSelector("form.login-form h2")).getText())
                .as("Проверка текста заголовка формы: Please sign in")
                .isEqualToIgnoringCase("Please sign in");

        System.out.println("\n=== Этап проверок обработки невалидных введенных кредов в форму аутентификации (негативный тест) (SUCCESS) ===\n");

        System.out.println("\n=== Тест 1.3 завершён успешно ===\n");

    }


    //==========================================================================================================================================


    //1.4. Проверить сохранение товаров в корзине после обновления страницы
    @Test
    @Tag("UI")
    @Order(4)
    void RefreshCheckAddGoodsIntoCartTest() {
        System.out.println("\n=== Тест 1.4: Проверить сохранение товаров в корзине после обновления страницы ===\n");

        //Добавление созданного в тесте 1.1 товара в корзину
        System.out.println("\n=== Этап поиска ранее созданного в тесте 1.1 товара: " + createdGoodName + " и добавление его в корзину ===\n");


        // Явное ожидание появления кнопки "В корзину" (до 2 секунд)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement addButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@data-action='add-to-cart'][@data-name='" + createdGoodName + "']")
                )
        );
        System.out.println("[INFO] Кнопка 'В корзину' найдена и доступна");

        // Формируем цикл/количество нажатий + находим по сочетанию имени ранее созданного товара и data-action, добавляем в корзину товар
        System.out.println("[INFO] Генерация количества нажатий на кнопку: " + addGoodAmountInCart);
        for (int i = 1; i <= addGoodAmountInCart; i++) {
            addButton.click();
            System.out.println("[INFO] Товар найден. Нажимаем кнопку 'В корзину' для товара: " + createdGoodName);
            System.out.println("[INFO] Нажатие " + i + " выполнено");
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\n=== Этап поиска ранее созданного в тесте 1.1 товара: " + createdGoodName + " и добавление его в корзину (SUCCESS) ===\n");

        System.out.println("[INFO] Обновление текущей страницы сайта");
        driver.navigate().refresh();//баг - после обновления страницы корзина очищается,
        // проверка всегда будет падать на поиске карточки товара


        System.out.println("[INFO] Открываем корзину всех товаров");
        driver.findElement(By.id("open-cart-btn")).click();
        System.out.println("[INFO] Корзина открыта");


        // Ищем карточку товара в корзине по ранее созданному имени добавленного товара
        System.out.println("\n=== Этап поиска товара в корзине и выполнения проверок ===\n");
        System.out.println("[INFO] Поиск карточки товара по ранее созданному имени товара: " + createdGoodName);
        WebElement cartItem = driver.findElement(
                By.xpath("//div[contains(@class, 'cart-item') and .//b[text()='" + createdGoodName + "']]")
        );
        System.out.println("[INFO] Карточка товара успешно найдена");

        // Проверка наименования добавленного товара = наименованию ранее созданного товара
        System.out.println("[INFO] Поиск и проверка имени: " + createdGoodName + " в карточке товара");
        WebElement itemName = cartItem.findElement(By.cssSelector("b"));
        assertThat(itemName.getText())
                .as(("Созданное имя должно отображаться в карточке товара в корзине: " + createdGoodName), createdGoodName)
                .isEqualTo(createdGoodName);
        System.out.println("[INFO] Имя соответствует: " + createdGoodName);


        // Проверка количества (должно соответстовать сгенерированном значению amount)
        System.out.println("[INFO] Поиск и проверка количества в карточке товара: ожидаемое " + addGoodAmountInCart);
        WebElement itemQty = cartItem.findElement(By.cssSelector(".qty-controls span"));
        assertThat(itemQty.getText())
                .as(("Количество товара в корзине должно быть = " + addGoodAmountInCart), addGoodAmountInCart)
                .isEqualTo(String.valueOf(addGoodAmountInCart));
        System.out.println("[INFO] Количество соответствует: " + addGoodAmountInCart);

        // Проверка суммы добавленного товара = прошлой сумме товаров в корзине
        System.out.println("[INFO] Поиск и проверка суммы товара в карточке товара: ожидаемая сумма = " + CartItemTotal);
        WebElement itemPrice = cartItem.findElement(By.cssSelector("div:nth-child(3)"));
        assertThat(itemPrice.getText())
                .as(("Сумма товара должна в корзине должна быть: " + CartItemTotal), CartItemTotal)
                .contains(String.valueOf(CartItemTotal));
        System.out.println("[INFO] Сумма соответствует: " + CartItemTotal);


        System.out.println("\n=== Этап поиска товара в корзине и выполнения проверок (SUCCESS) ===\n");//баг - после обновления страницы корзина очищается,
        // проверка всегда будет падать на поиске карточки товара

        System.out.println("\n=== Тест 1.4 завершён успешно ===\n"); //баг - после обновления страницы корзина очищается,
        // проверка всегда будет падать на поиске карточки товара


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