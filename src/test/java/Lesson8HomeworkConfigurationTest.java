import Config.ApiConfig;
import Config.ConfigProvider;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.SelenideElement;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import java.time.Duration;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.anyOf;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Lesson8HomeworkConfigurationTest {
    private static final Logger logger = Logger.getLogger(Lesson8HomeworkConfigurationTest.class.getName());
    private static final ApiConfig config = ConfigProvider.apiProps;

    private final Random random = new Random();

    public record Good(String name, Double price) {}

    private static RequestSpecification basicRQ;
    private static String createdGoodName;
    private static Double createdGoodPrice;
    private static int createdGoodId;

    /*
     Критерий 1.3: вывод всех параметров конфига перед запуском тестов.
     Логин и пароль НЕ выводятся.
     */

    @BeforeAll
    static void printConfig() {
        logger.log(Level.INFO, "=================================================");
        logger.log(Level.INFO, "Запуск автотестов с параметрами:");
        logger.log(Level.INFO, "UI_URL:      {0}", config.url());
        logger.log(Level.INFO, "API_URL:     {0}", config.apiUrl());
        logger.log(Level.INFO, "TIMEOUT:     {0} ms", config.timeout());
        logger.log(Level.INFO, "API_LOGS:        {0}", config.apiLogs());
        logger.log(Level.INFO, "GOOD_NAME:   {0}", config.goodName());
        logger.log(Level.INFO, "GOOD_PRICE:  {0}", String.format(Locale.ROOT, "%.2f", config.goodPrice()));
        logger.log(Level.INFO, "=================================================");

        logger.log(Level.INFO, "Конфиг загружен: URL={0}, API_URL={1}, TIMEOUT={2}, API_LOGS={3}, GOOD_NAME={4}, GOOD_PRICE={5}",
                new Object[]{config.url(), config.apiUrl(), config.timeout(),
                        config.apiLogs(), config.goodName(), String.format(Locale.ROOT, "%.2f", config.goodPrice())});
    }


    /*
     Критерий 1.2: применение параметров конфига в ходе запуска автотестов.
     */

    @BeforeEach
    void setUp() {
        // Тайм-аут Selenide
        Configuration.timeout = config.timeout();

        // URL UI
        Configuration.baseUrl = config.url();

        // URL API + Credentials
        basicRQ = new RequestSpecBuilder()
                .setBaseUri(config.apiUrl())
                .setAuth(RestAssured.preemptive()
                        .basic(config.adminLogin(), config.adminPassword()))
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        logger.log(Level.INFO, "=== Подготовка тестовых данных ===");

        // Имя делаем уникальным, чтобы не конфликтовать с уже существующими товарами
        String name = config.goodName() + "-" + System.currentTimeMillis();
        Double price = config.goodPrice();

        Response response = given()
                .spec(basicRQ)
                .body(new Good(name, price))
                .when()
                .post("/goods/add")
                .then()
                .log().all()
                .statusCode(anyOf(is(200), is(201)))
                .body("message", equalTo("success"))
                .extract().response();

        createdGoodName = name; // Сохраним имя для будущих ассертов
        createdGoodPrice = price; // Сохраним цену для будущих ассертов
        createdGoodId = response.jsonPath().getInt("data.id"); // Сохраним ID для будущих тестов и ассертов
        logger.log(Level.INFO, "Товар создан, имя: {0}, id: {1}",
                new Object[]{createdGoodName, createdGoodId});// Сохраним ID для будущих тестов

        logger.log(Level.INFO, "Test method start\n" +
                "=================================================\n" +
                "Запуск браузера и открытие сайта\n");
        open(config.url());
        logger.log(Level.INFO, "=== Подготовка завершена ===");
    }

    @AfterEach
    void cleanUp() {
        logger.log(Level.INFO, "=== Начало очистки памяти и закрытия браузера ===");
        logger.log(Level.INFO, "Удаление товара с id = {0}", createdGoodId);
        given()
                .spec(basicRQ)
                .pathParam("id", createdGoodId)
                .when()
                .delete("/goods/{id}")
                .then()
                .log().all()
                .statusCode(anyOf(is(200), is(204)))
                .body(containsString("deleted successfully"));

        logger.log(Level.INFO, "Ранее сгенерированный товар с именем: " + createdGoodName + " и " + "id товара: " + createdGoodId + " удалён успешно");

        logger.log(Level.INFO, "Test method end\n" +
                "==================================\n" +
                "Закрытие браузера и очистка памяти\n");
        closeWebDriver();
        logger.log(Level.INFO, "=== Очистка памяти и закрытия браузера завершено ===");
    }

    //Переиспользовал прошлую задачу Lesson7HomeworkBP

    //1.1. Перетащить элемент в корзину с помощью Drag-and-Drop.

    @Test
    @Tag("UI")
    @Order(1)
    void addGoodsIntoCartDnD() {

        logger.log(Level.INFO, "=== Тест 1.1: Перетаскивание в корзину созданного в предусловии товара: " + createdGoodName + " ===");

        SelenideElement cardCreatedGoodName = $x("//*[@data-name='" + createdGoodName + "']");
        SelenideElement cartButton = $x("//*[@id = 'open-cart-btn']");

        logger.log(Level.INFO, "Ожидание видимости карточки товара и корзины");
        cardCreatedGoodName.should(visible);
        cartButton.should(visible);

        logger.log(Level.INFO, "Этап создания количества DnD в корзину с рандомными значениями");
        Integer addGoodAmountInCartDnD = random.nextInt(3, 5);
        logger.log(Level.INFO, "Генерация количества DnD: " + addGoodAmountInCartDnD);

        logger.log(Level.INFO, "Выполнение drag-and-drop");

        // Формируем цикл DnD + находим по сочетанию имени ранее созданного товара и data-action, добавляем в корзину товар
        for (int i = 1; i <= addGoodAmountInCartDnD; i++) {
            cardCreatedGoodName.dragAndDrop(DragAndDropOptions.to(cartButton));
            sleep(200);
            logger.log(Level.INFO, "Товар найден. переносим карточку товара: " + createdGoodName + " в корзину");
            logger.log(Level.INFO, "DnD/перенос карточки товара выполнен " + i + " раз");
        }


        logger.log(Level.INFO, "Проверка, что товар появился в корзине");
        SelenideElement openCart = $x("//*[@id = 'open-cart-btn']");
        openCart.click();
        logger.log(Level.INFO, "Корзина товаров открыта");

        logger.log(Level.INFO, "Поиск карточки товара по ранее созданному имени товара: " + createdGoodName);
        SelenideElement cartItem = $$(".cart-item")
                .findBy(Condition.text(createdGoodName));
        logger.log(Level.INFO, "Карточка товара успешно найдена");

        logger.log(Level.INFO, "Поиск и проверка имени: " + createdGoodName + " в карточке товара");
        cartItem.$("b").should(text(createdGoodName));
        logger.log(Level.INFO, "Имя соответствует: " + createdGoodName);

        logger.log(Level.INFO, "Поиск и проверка количества товара: ожидаемое значение " + addGoodAmountInCartDnD + " в карточке товара");
        cartItem.$(".qty-controls span").should(text(String.valueOf(addGoodAmountInCartDnD)));
        logger.log(Level.INFO, "Количество соответствует: " + addGoodAmountInCartDnD);

        double expectedCartItemTotal = createdGoodPrice*addGoodAmountInCartDnD;
        logger.log(Level.INFO, "Поиск и проверка суммы товара: " + expectedCartItemTotal + " в карточке товара");
        cartItem.$("div:nth-child(3)").should(partialText(String.valueOf(expectedCartItemTotal)));
        logger.log(Level.INFO, "Сумма товара соответствует: " + expectedCartItemTotal);


        logger.log(Level.INFO, "Товар " + createdGoodName + " успешно добавлен в корзину");

        logger.log(Level.INFO, "=== Тест 1.1 завершён успешно ===");

    }

//==========================================================================================================================================

// 1.2. Удалить добавленный элемент из корзины и проверить, что он там больше не отображается.

    @Test
    @Tag("UI")
    @Order(2)
    void removeItemFromCartTest() {
        logger.log(Level.INFO, "=== Тест 1.2: Удаление ранее добавленного товара: " + createdGoodName + " из корзины ===");

        // Вызываем метод теста 1.1, который добавляет товар в корзину и проверяет его
        logger.log(Level.INFO, "Эмуляция шагов задачи 1.1 - перетащить элемент в корзину с помощью Drag-and-Drop");
        addGoodsIntoCartDnD();

        // После выполнения метода 1.1 корзина открыта, и товар в ней есть
        // Находим элемент корзины с нашим товаром
        logger.log(Level.INFO, "Поиск карточки товара по ранее созданному имени товара: " + createdGoodName);
        SelenideElement cartItem = $$(".cart-item")
                .findBy(Condition.text(createdGoodName));
        cartItem.should(visible);
        logger.log(Level.INFO, "Карточка товара успешно найдена");

        // Ищем кнопку удаления
        SelenideElement removeButton = cartItem.$("[data-action='remove']");
        logger.log(Level.INFO, "Нажатие кнопки удаления");
        removeButton.should(visible).click();

        // Проверяем, что товар исчез из корзины
        logger.log(Level.INFO, "Проверка отсутствия товара в корзине");
        cartItem.shouldNot(visible);
        logger.log(Level.INFO, "Товар " + createdGoodName + " удалён из корзины");

        logger.log(Level.INFO, "=== Тест 1.2 завершён успешно ===");
    }


    //1.3. Добавить товар через админку, выйти на витрину и проверить, что товар отображается.
    @Test
    @Tag("UI")
    @Order(3)
    void AddGoodAndCheckTest(){
        logger.log(Level.INFO, "=== Тест 1.3: Добавить товар через админку, выйти на витрину и проверить, что товар отображается ===");

        // === Этап входа в админку ===
        logger.log(Level.INFO, "Этап входа в админку: переход на страницу");
        $("[href='/admin']").click();

        logger.log(Level.INFO, "Ввод валидного логина");
        $("#username").setValue(config.adminLogin());

        logger.log(Level.INFO, "Ввод валидного пароля");
        $("#password").setValue(config.adminPassword());

        logger.log(Level.INFO, "Нажатие кнопки входа");
        $x("//*[@type='submit']").click();

        logger.log(Level.INFO, "=== Этап входа в админку (SUCCESS) ===");

        // === Этап добавления товара через админку ===
        logger.log(Level.INFO, "Этап добавления товара через админку");

        logger.log(Level.INFO, "Ввод имени товара: {0}", createdGoodName);
        $x("//*[@id = 'n-name']").sendKeys(createdGoodName);

        logger.log(Level.INFO, "Ввод цены товара: {0}", createdGoodPrice);
        $x("//*[@id = 'n-price']").sendKeys(String.valueOf(createdGoodPrice));

        logger.log(Level.INFO, "Нажатие кнопки 'Создать'");
        $x("//*[@id = 'add-btn']").click();

        // проверка тоста об успешном добавлении товара (опционально, захотелось для полноты покрытия)
        logger.log(Level.INFO, "Ожидание отсутствия появления тоста об успешном создании товара (hidden-скрыт)...");
        $x("//*[@id = 'toast-container']").shouldNot(Condition.visible, Duration.ofSeconds(1));
        $x("//*[@id = 'toast-container']").should(exist);


        logger.log(Level.INFO, "Тост соответствует ожидаемому (тост скрыт)");

        logger.log(Level.INFO, "Этап добавления товара через админку (SUCCESS) ===");

        //выход из админки
        logger.log(Level.INFO, "Выход из админки - нажать 'Вернуться на сайт'");
        $x("//*[@href= '/']").click();
        logger.log(Level.INFO, "=== Выход из админки (SUCCESS) ===");

        // находим карточку именно нашего товара по data-name
        logger.log(Level.INFO, "Этап поиска товара на витрине по data-name: {0}", createdGoodName);
        //Пришлось делать ожидание 2 сек - иногда фейлится
        sleep(2000);
        SelenideElement productCard = $$(".product-card")
                .findBy(Condition.attribute("data-name", createdGoodName));
        productCard.shouldBe(visible);
        logger.log(Level.INFO, "Карточка товара успешно найдена");

        // проверка имени
        logger.log(Level.INFO, "[INFO] Поиск и проверка имени: " + createdGoodName + " в карточке товара");
        productCard.$("h4").should(text(createdGoodName));
        logger.log(Level.INFO, "Имя соответствует: {0}", createdGoodName);


        // проверка цены
        logger.log(Level.INFO, "Проверка цены в карточке товара: {0}", createdGoodPrice);
        productCard.$("div:not([class])").should(partialText(String.valueOf(createdGoodPrice)));
        logger.log(Level.INFO, "Цена соответствует: {0}", createdGoodPrice);

        logger.log(Level.INFO, "=== Этап поиска товара и выполнения проверок (SUCCESS) ===");

        logger.log(Level.INFO, "=== Тест 1.3 завершён успешно ===");
    }

}


















/*
Задача 1: добавить в проект конфигурационный файл, а также обработать его в программном коде,
обеспечив считывание из него и использование в ходе запуска автотестов следующих параметров:

URL стенда и API;
тайм-аут для поиска элементов;
режим логирования (можно без реализации);
Credentials для входа в админку;
имя и цена для стартового товара.

Все параметры, которые считываются из конфига, должны выводиться в консоль перед запуском теста, кроме логина и пароля.

Формат результата

Создать публичный репозиторий на GitHub и приложить ссылку на коммит.

Критерии проверки

Задача 1:

1.1* (обязательная) В проект добавлен конфигурационный файл со всеми параметрами — 3 б.

1.2* (обязательная) Конфигурационный файл обрабатывается в коде запуска автотестов — 4 б.

1.3 Все параметры, кроме Credentials, выводятся на экран перед запуском тестов — 2 б.
 */