import Config.ApiConfig;
import Config.ConfigProvider;
import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.anyOf;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Lesson9HomeworkPageObjectTest {
    private static final Logger logger = Logger.getLogger(Lesson9HomeworkPageObjectTest.class.getName());
    private static final ApiConfig config = ConfigProvider.apiProps;

    private final Random random = new Random();

    public record Good(String name, Double price) {
    }

    private static RequestSpecification basicRQ;
    private static String createdGoodName;
    private static Double createdGoodPrice;
    private static int createdGoodId;

    // Список всех созданных товаров для удаления в @AfterEach
    private static final List<Integer> goodsToDelete = new ArrayList<>();

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
                        config.apiLogs(), config.goodName(),
                        String.format(Locale.ROOT, "%.2f", config.goodPrice())});
    }


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

        // Уникальное имя на основе значения из конфига
        String name = config.goodName() + "-" + System.currentTimeMillis();
        Double price = config.goodPrice() + random.nextDouble(10.0, 30.0);;

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
        goodsToDelete.add(createdGoodId);// ← добавляем в общий список

        logger.log(Level.INFO, "Товар создан, имя: {0}, id: {1}",
                new Object[]{createdGoodName, createdGoodId});// Сохраним ID для будущих тестов

        logger.log(Level.INFO, "Test method start\n" +
                "=================================================\n" +
                "Запуск браузера и открытие сайта\n");

        new Lesson9PageObjectMainPage().open(config.url());
        logger.log(Level.INFO, "=== Подготовка завершена ===");
    }

    @AfterEach
    void cleanUp() {
        logger.log(Level.INFO, "=== Начало очистки памяти и закрытия браузера ===");
        logger.log(Level.INFO, "Удаление сгенерированных товаров, товаров к удалению = {0}", goodsToDelete.size());

        for (int id : goodsToDelete) {
            given()
                    .spec(basicRQ)
                    .pathParam("id", id)
                    .when()
                    .delete("/goods/{id}")
                    .then()
                    .log().all()
                    .statusCode(anyOf(is(200), is(204)))
                    .body(containsString("deleted successfully"));
        }

        goodsToDelete.clear();
        logger.log(Level.INFO, "Список товаров для удаления очищен");

        logger.log(Level.INFO, "Test method end\n" +
                "==================================\n" +
                "Закрытие браузера и очистка памяти\n");
        closeWebDriver();
        logger.log(Level.INFO, "=== Очистка памяти и закрытия браузера завершено ===");
    }

    //==========================================================================================================================================

    // 2.1. Добавить три единицы товара в корзину и оплатить их (общая стоимость не должна превышать 300 рублей).
    // Проверить уведомление об обработке заказа.

    @Test
    @Tag("UI")
    @Order(1)
    void AddGoodsIntoCartAndPayTest() {
        logger.log(Level.INFO, "=== Тест 2.1: Добавить 3 единицы товара и оплатить ===");

        Lesson9PageObjectMainPage page = new Lesson9PageObjectMainPage();

        // Проверка, что основные элементы главной страницы видны
        new Lesson9PageObjectMainPageAssert(page)
                .cartButtonIsVisible()
                .adminLinkIsVisible();
        logger.log(Level.INFO, "Основные элементы главной страницы видны (корзина и администрирование)");

        for (int i = 1; i <= 3; i++) {
            page.clickAddToCart(createdGoodName);
            logger.log(Level.INFO, "Товар " + createdGoodName + " добавлен в корзину, нажатие {0}", i);
        }

        page.openCart();
        logger.log(Level.INFO, "Корзина открыта");

        new Lesson9PageObjectMainPageAssert(page)
                .cartItemVisible(createdGoodName)
                .qtyEquals(createdGoodName, 3)
                .itemTotalContains(createdGoodName, createdGoodPrice * 3);
        logger.log(Level.INFO, "Проверки добавления товара в корзину успешно пройдены");

        // Перед оформлением заказа убеждаемся, что кнопка доступна
        new Lesson9PageObjectMainPageAssert(page)
                .makeOrderButtonIsVisible();
        logger.log(Level.INFO, "Кнопка Оформить заказ доступна");

        page.clickMakeOrder();
        logger.log(Level.INFO, "Проверка тоста об оформлении заказа");
        new Lesson9PageObjectMainPageAssert(page)
                .toastHasText("Заказ принят в обработку!");

        logger.log(Level.INFO, "=== Тест 2.1 завершён успешно ===");
    }

    //==========================================================================================================================================

    // 2.2. Добавить в корзину несколько разных товаров и проверить, что общая цена в корзине считается корректно.

    @Test
    @Tag("UI")
    @Order(2)
    void addSeveralDifferentGoodsAndCheckTotalPriceTest() {
        logger.log(Level.INFO, "=== Тест 2.2: Добавить в корзину несколько разных товаров и проверить, что общая цена в корзине считается корректно. ===");

    // Создаём еще один товар с рандомными значениями имени и цены через API
        logger.log(Level.INFO, "=== Этап создания еще одного товара с рандомными значениями имени и цены ===");

        String secondName = "Second-" + System.currentTimeMillis();
        Double secondPrice = random.nextDouble(10.0, 99.0);

        Response resp = given()
                .spec(basicRQ)
                .body(Map.of("name", secondName, "price", secondPrice))
                .when().
                post("/goods/add")
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .extract().response();

        int secondId = resp.jsonPath().getInt("data.id"); // Прихраним ID чтобы добавить к удалению
        goodsToDelete.add(secondId); // Прихраним ID в общий список — @AfterEach удалит

        logger.log(Level.INFO, "Второй товар создан: имя={0}, id={1}",
                new Object[]{secondName, secondId});

        logger.log(Level.INFO, "=== Этап создания еще одного товара с рандомными значениями имени и цены (SUCCESS) ===");


        Lesson9PageObjectMainPage page = new Lesson9PageObjectMainPage()
                .refreshPage();
        logger.log(Level.INFO, "=== Обновление текущей страницы сайта ===");

        // Проверка, что основные элементы главной страницы видны
        new Lesson9PageObjectMainPageAssert(page)
                .cartButtonIsVisible()
                .adminLinkIsVisible();
        logger.log(Level.INFO, "Основные элементы главной страницы видны (корзина и администрирование)");

        page.clickAddToCart(createdGoodName);
        page.clickAddToCart(secondName);
        page.openCart();
        logger.log(Level.INFO, "Оба товара успешно добавлены в корзину, корзина открыта");

        new Lesson9PageObjectMainPageAssert(page)
                .cartItemVisible(createdGoodName)
                .cartItemVisible(secondName)
                .totalPriceContains(String.valueOf(createdGoodPrice + secondPrice))
                .makeOrderButtonIsVisible();
        logger.log(Level.INFO, "Проверки добавления товаров в корзину успешно пройдены");

        logger.log(Level.INFO, "Проверка общей суммы пройдена: {0}",
                createdGoodPrice + secondPrice);

        logger.log(Level.INFO, "=== Тест 2.2 завершён успешно ===");
    }

    //==========================================================================================================================================

    // 2.3. Войти в админку и добавить товар. Проверить уведомление после добавления товара.

    @Test
    @Tag("UI")
    @Order(3)
    void addGoodThrowAdminAndCheckNotificationTest(){
        logger.log(Level.INFO, "=== Тест 2.3: Войти в админку и добавить товар. Проверить уведомление после добавления товара. ===");

    // Создаём товар с рандомными значениями имени и цены для добавления через UI
        logger.log(Level.INFO, "=== Этап создания еще одного товара с рандомными значениями имени и цены ===");
        String uiGoodName = "UI-" + System.currentTimeMillis();
        Double uiGoodPrice = random.nextDouble(1.0, 99.0);
        logger.log(Level.INFO, "Сгенерирован новый товар: имя={0}, цена={1}",
                new Object[]{uiGoodName, uiGoodPrice});
        logger.log(Level.INFO, "=== Этап создания еще одного товара с рандомными значениями имени и цены (SUCCESS) ===");


        Lesson9PageObjectMainPage mainPage = new Lesson9PageObjectMainPage();
        // Проверка, что основные элементы главной страницы видны
        new Lesson9PageObjectMainPageAssert(mainPage)
                .adminLinkIsVisible();
        logger.log(Level.INFO, "Основные элементы главной страницы видны (администрирование)");


        logger.log(Level.INFO, "=== Переход на страницу админки ===");
        Lesson9PageObjectAdminPanelPage adminPage = mainPage.goToAdmin();

        new Lesson9PageObjectAdminPanelPageAssert(adminPage)
                .loginFieldVisible()
                .passwordFieldVisible()
                .createButtonVisible();
        logger.log(Level.INFO, "Форма аутентификации проверена");


        logger.log(Level.INFO, "Заполнение формы аутентификации валидными данными");
        adminPage
                .setUsername(config.adminLogin()) // Ввод валидного логина
                .setPassword(config.adminPassword()); // Ввод валидного пароля

        logger.log(Level.INFO, "Проверка заполнения логина и пароля валидными данными");
        new Lesson9PageObjectAdminPanelPageAssert(adminPage)
                .usernameHasText(config.adminLogin())
                .passwordHasText(config.adminPassword());

        adminPage
                .submit(); // Нажатие кнопки входа
        logger.log(Level.INFO, "Вход в админку выполнен");

        logger.log(Level.INFO, "Заполнение формы добавления имени и цены товара в админке");
        adminPage
                .createGood(uiGoodName, uiGoodPrice);

        logger.log(Level.INFO, "Проверка тоста об успешном добавлении товара");
        new Lesson9PageObjectAdminPanelPageAssert(adminPage)
                .toastHasText("Товар успешно добавлен!");


        // Получение id созданного через UI товара через API (GET - запрос), чтобы удалить в @AfterEach
        Response resp = given()
                .spec(basicRQ)
                .queryParam("page", 0)
                .queryParam("size", 1000)
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        List<Integer> ids = resp.jsonPath().getList("goods.findAll { it.name == '" + uiGoodName + "' }.id");
        if (!ids.isEmpty()) {
            goodsToDelete.add(ids.get(0));
            logger.log(Level.INFO, "Товар из UI добавлен в список на удаление: id={0}", ids);
        }

        logger.log(Level.INFO, "=== Тест 2.3 завершён успешно ===");
    }


    //==========================================================================================================================================


    //2.4. Войти в админку и отредактировать товар. Выйти на список товаров и проверить, что изменения применились.

    @Test
    @Tag("UI")
    @Order(4)
    void editGoodThroughAdminAndCheckChangesTest() {
        logger.log(Level.INFO, "=== Тест 2.4: Войти в админку и отредактировать товар. Выйти на список товаров и проверить, что изменения применились. ===");

        // Создаём товар с рандомными значениями имени и цены для добавления через UI
        logger.log(Level.INFO, "=== Этап создания еще одного товара с рандомными значениями имени и цены ===");
        String editGoodName = "Edited-" + System.currentTimeMillis();
        Double editGoodPrice = random.nextDouble(1.0, 99.0);

        logger.log(Level.INFO, "Сгенерирован новый товар: имя={0}, цена={1}",
                new Object[]{editGoodName, editGoodPrice});
        logger.log(Level.INFO, "=== Этап создания еще одного товара с рандомными значениями имени и цены (SUCCESS) ===");

        Lesson9PageObjectMainPage mainPage = new Lesson9PageObjectMainPage();
        // Проверка, что основные элементы главной страницы видны
        new Lesson9PageObjectMainPageAssert(mainPage)
                .adminLinkIsVisible();
        logger.log(Level.INFO, "Основные элементы главной страницы видны (администрирование)");

        logger.log(Level.INFO, "=== Переход на страницу админки ===");
        Lesson9PageObjectAdminPanelPage adminPage = mainPage.goToAdmin();

        new Lesson9PageObjectAdminPanelPageAssert(adminPage)
                .loginFieldVisible()
                .passwordFieldVisible()
                .createButtonVisible();
        logger.log(Level.INFO, "Форма аутентификации проверена");


        logger.log(Level.INFO, "Заполнение формы аутентификации валидными данными");
        adminPage
                .setUsername(config.adminLogin()) // Ввод валидного логина
                .setPassword(config.adminPassword()); // Ввод валидного пароля

        logger.log(Level.INFO, "Проверка заполнения логина и пароля валидными данными");
        new Lesson9PageObjectAdminPanelPageAssert(adminPage)
                .usernameHasText(config.adminLogin())
                .passwordHasText(config.adminPassword());

        adminPage
                .submit(); // Нажатие кнопки входа
        logger.log(Level.INFO, "Вход в админку выполнен");


        // === Этап проверки исходного состояния ===
        logger.log(Level.INFO, "Проверка, что строка товара с id={0} отображается", createdGoodId);
        new Lesson9PageObjectAdminPanelPageAssert(adminPage)
                .goodRowVisible(createdGoodId)
                .goodNameEquals(createdGoodId, createdGoodName)
                .goodPriceEquals(createdGoodId, String.valueOf(createdGoodPrice));
        logger.log(Level.INFO, "Исходные имя и цена в таблице совпадают");


        // === Этап редактирования ===
        logger.log(Level.INFO, "Редактирование: имя → {0}, цена → {1}",
                new Object[]{editGoodName, editGoodPrice});
        adminPage.editGood(createdGoodId, editGoodName, editGoodPrice);

        new Lesson9PageObjectAdminPanelPageAssert(adminPage)
                .toastHasText("Товар #" + createdGoodId + " обновлен");
        logger.log(Level.INFO, "Тост об успешном обновлении подтверждён");


        // === Этап проверки отображения параметров отредактированного товара на витрине ===
        logger.log(Level.INFO, "Выход на витрину");
        Lesson9PageObjectMainPage storefront = adminPage.goToStorefront()
        .refreshPage();
        logger.log(Level.INFO, "=== Обновление текущей страницы сайта ===");

        new Lesson9PageObjectMainPageAssert(storefront)
                .productCardIsVisible(editGoodName)
                .productNameEquals(editGoodName)
                .productPriceContains(editGoodName, editGoodPrice)
                .cartButtonIsVisible()
                .adminLinkIsVisible();
        logger.log(Level.INFO, "Карточка с новым именем и ценой отображается на витрине. Проверка успешна");

        // Обновляем сохранённые данные (id тот же, @AfterEach удалит по нему)
        createdGoodName = editGoodName;
        createdGoodPrice = editGoodPrice;

        logger.log(Level.INFO, "=== Тест 2.4 завершён успешно ===");
    }

    }















/*
Задание

ВЕБИНАР

Задача 2: переписать (или написать заново) следующие кейсы в проекте с использованием архитектуры PageObject:

2.1. Добавить три единицы товара в корзину и оплатить их (общая стоимость не должна превышать 300 рублей). Проверить уведомление об обработке заказа.

2.2. Добавить в корзину несколько разных товаров и проверить, что общая цена в корзине считается корректно.

2.3. Войти в админку и добавить товар. Проверить уведомление после добавления товара.

2.4. Войти в админку и отредактировать товар. Выйти на список товаров и проверить, что изменения применились.

Все проверки в кейсах должны быть организованы в классах с ассертами (PageAssert).

Задача 2:

2.1–2.4* (обязательные) Проект содержит автотесты для указанных кейсов — 3 б. / каждый

2.5 В автотесте нет взаимодействия с элементами, объявленными вне PageObject — 2 б.

2.6 Все проверки вынесены в PageAssert-класс — 3 б.
 */