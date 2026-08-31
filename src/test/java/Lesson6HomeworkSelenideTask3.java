import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.codeborne.selenide.Condition.partialText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Lesson6HomeworkSelenideTask3 {

    private final Random random = new Random();

    public record Good (String name, Double price) {} //для запроса POST /goods/add с параметрами body

    private RequestSpecification BasicRQ = new RequestSpecBuilder()
            .setBaseUri("http://localhost:8080")
            .setAuth(RestAssured.preemptive().basic("admin", "secret123"))
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    // Сохраняем данные созданного товара и цены, количества, итоговой суммы корзины для использования в проверках
    private static String createdGoodName;
    private static Double createdGoodPrice;
    private static int createdGoodId;

    // Список всех созданных товаров для удаления в @AfterEach
    private static final List<Integer> goodsToDelete = new ArrayList<>();


    @BeforeEach
    void setUp(){
        // Создаём товар с рандомными значениями имени и цены
        String name = "Good-" + UUID.randomUUID().toString().substring(0, 8);
        Double price = random.nextDouble(1.0, 99.0);
        Response response = given() //через объект response
                .spec(BasicRQ)
                .body(new Good(name, price))
                .when()
                .post("/goods/add")
                .then()
                .log().all()
                .statusCode(anyOf(is(200), is(201)))   // принимает 200 ИЛИ 201 код
                .body("message", equalTo("success"))   // <-- ассерт на success
                .extract().response();


        createdGoodId = response.jsonPath().getInt("data.id"); // Сохраним ID для будущих тестов
        createdGoodName = name; // Сохраним имя для будущих ассертов
        createdGoodPrice = price; // Сохраним цену для будущих ассертов
        goodsToDelete.add(createdGoodId);

        System.out.println("Test method start\n" +
                "=================================================\n" +
                "Запуск браузера и открытие сайта\n");
        open("http://localhost:8080/");

    }


    @AfterEach
    void cleanUp(){
        for (int id : goodsToDelete) {
            given()
                    .spec(BasicRQ)
                    .pathParam("id", id)
                    .when()
                    .delete("/goods/{id}")
                    .then()
                    .log().all()
                    .statusCode(anyOf(is(200), is(204)))   // принимает 200 ИЛИ 204 код
                    .body(containsString("deleted successfully"));
        }

        goodsToDelete.clear();//Удаляем общий список сгенерированных товаров для теста
        System.out.println("Test method end\n" +
                "==================================\n" +
                "Закрытие браузера и очистка памяти\n");
        closeWebDriver();
    }

    //==========================================================================================================================================


//3.1. Добавить три единицы товара в корзину и оплатить их (общая стоимость не должна превышать 300 рублей). Проверить уведомление об обработке заказа.

    @Test
    @Tag("UI")
    @Order(1)
    void AddGoodsIntoCartAndPayTest() {

        System.out.println("\n=== Тест 3.1: Добавить три единицы товара в корзину и оплатить их (общая стоимость не должна превышать 300 рублей). Проверить уведомление об обработке заказа. ===\n");

        System.out.println("\n=== Этап поиска ранее созданного товара: " + createdGoodName + " и добавление его в корзину ===\n");

        // Явное ожидание появления кнопки "В корзину" (до 2 секунд)
        sleep(2000);
        SelenideElement addButton = $x("//*[@data-action='add-to-cart'][@data-name='" + createdGoodName + "']");
        System.out.println("[INFO] Кнопка 'В корзину' найдена и доступна");
        //находим по сочетанию имени ранее созданного товара и data-action, добавляем в корзину товар
        // Формируем цикл/количество нажатий + находим по сочетанию имени ранее созданного товара и data-action, добавляем в корзину товар
        for (int i = 1; i <= 3; i++) {
            addButton.click();
            sleep(200);
            System.out.println("[INFO] Товар найден. Нажимаем кнопку 'В корзину' для товара: " + createdGoodName);
            System.out.println("[INFO] Нажатие " + i + " выполнено");
        }

        System.out.println("\n=== Этап поиска ранее созданного товара: " + createdGoodName + " и добавление его в корзину (SUCCESS) ===\n");

        System.out.println("[INFO] Открываем корзину всех товаров");
        SelenideElement openCart = $x("//*[@id = 'open-cart-btn']");
        openCart.click();
        System.out.println("[INFO] Корзина открыта");

        System.out.println("\n=== Этап оформления заказа для товара: " + createdGoodName + " ===\n");

        System.out.println("[INFO] Нажимаем на кнопку Оформить заказ");
        $("#makeOrder").click();

        System.out.println("[INFO] Проверка уведомления оформления заказа");
        SelenideElement toastVisible = $x("//*[@id = 'toast-container']");
        toastVisible.should(Condition.visible, Duration.ofSeconds(1));
        toastVisible.should(text("Заказ принят в обработку!"));

        System.out.println("[INFO] Проверка тоста об оформлении заказа. Тост соответствует");

        System.out.println("\n=== Этап оформления заказа для товара: " + createdGoodName + " (SUCCESS) ===\n");

        System.out.println("\n=== Тест 3.1 завершён успешно ===\n");
    }

//==========================================================================================================================================

// 3.2. Добавить в корзину несколько разных товаров и проверить, что общая цена в корзине считается корректно.

    @Test
    @Tag("UI")
    @Order(2)
    void addSeveralDifferentGoodsAndCheckTotalPriceTest() {

        System.out.println("\n=== Тест 3.2: Добавить в корзину несколько разных товаров и проверить, что общая цена в корзине считается корректно. ===\n");


        // Создаём еще один товар с рандомными значениями имени и цены
        System.out.println("\n=== Этап создания еще одного товара с рандомными значениями имени и цены ===\n");
        String name = "Good-" + UUID.randomUUID().toString().substring(0, 8);
        Double price = random.nextDouble(1.0, 99.0);
        Response response = given() //через объект response
                .spec(BasicRQ)
                .body(new Good(name, price))
                .when()
                .post("/goods/add")
                .then()
                .log().all()
                .statusCode(anyOf(is(200), is(201)))   // принимает 200 ИЛИ 201 код
                .body("message", equalTo("success"))   // <-- ассерт на success
                .extract().response();


        Integer createdGoodSecondId = response.jsonPath().getInt("data.id"); // Прихраним ID для удаления его через API
        goodsToDelete.add(createdGoodSecondId); // добавляем в общий список для удаления

        System.out.println("\n=== Этап создания еще одного товара с рандомными значениями имени и цены (SUCCESS) ===\n");

        System.out.println("[INFO] Обновление текущей страницы сайта");
        refresh();

        //Добавление созданных товаров в корзину
        System.out.println("\n=== Этап поиска ранее созданных товаров: " + createdGoodName +  ", " + name +  " и добавление их в корзину ===\n");

        // Явное ожидание появления кнопки "В корзину" (до 2 секунд)
        sleep(2000);
        //Добавление 1 товара (из предусловия)
        SelenideElement addButtonFirstGood = $x("//*[@data-action='add-to-cart'][@data-name='" + createdGoodName + "']");
        System.out.println("[INFO] Кнопка 'В корзину' найдена и доступна");
        //находим по сочетанию имени ранее созданного товара и data-action, добавляем в корзину товар
        addButtonFirstGood.click();
        sleep(300); // небольшая пауза
        System.out.println("[INFO] Товар найден. Нажимаем кнопку 'В корзину' для товара: " + createdGoodName);
        System.out.println("[INFO] Нажатие выполнено");

        // Явное ожидание появления кнопки "В корзину" (до 2 секунд)
        sleep(2000);
        //Добавление 2 товара (сгенерированного в тесте 3.2)
        SelenideElement addButtonSecondGood = $x("//*[@data-action='add-to-cart'][@data-name='" + name + "']");
        System.out.println("[INFO] Кнопка 'В корзину' найдена и доступна");
        //находим по сочетанию имени ранее созданного товара и data-action, добавляем в корзину товар
        addButtonSecondGood.click();
        sleep(300); // небольшая пауза
        System.out.println("[INFO] Товар найден. Нажимаем кнопку 'В корзину' для товара: " + name);
        System.out.println("[INFO] Нажатие выполнено");


        System.out.println("\n=== Этап поиска ранее созданных товаров: " + createdGoodName +  ", " + name +  " и добавление их в корзину (SUCCESS) ===\n");


        System.out.println("[INFO] Открываем корзину всех товаров");
        SelenideElement openCart = $x("//*[@id = 'open-cart-btn']");
        openCart.click();
        System.out.println("[INFO] Корзина открыта");


        // Ищем карточку товара №1 в корзине по ранее созданному имени добавленного товара
        System.out.println("\n=== Этап поиска товаров в корзине и выполнения проверок ===\n");

        System.out.println("[INFO] Поиск карточки товара по ранее созданному имени товара: " + createdGoodName);
        SelenideElement cartItem = $$(".cart-item")
                .findBy(Condition.text(createdGoodName));
        System.out.println("[INFO] Карточка товара №1 успешно найдена");

        // Проверка наименования добавленного товара №1 = наименованию ранее созданного товара
        System.out.println("[INFO] Поиск и проверка имени: " + createdGoodName + " в карточке товара");
        cartItem.$("b").should(text(createdGoodName));
        System.out.println("[INFO] Имя №1 соответствует: " + createdGoodName);

        // Проверка цены добавленного товара №1
        System.out.println("[INFO] Поиск и проверка суммы товара в карточке товара: ожидаемая сумма = " + createdGoodPrice);
        cartItem.$("div:nth-child(3)").should(partialText(String.valueOf(createdGoodPrice)));
        System.out.println("[INFO] Cумма товара №1 соответствует: " + createdGoodPrice);



        // Ищем карточку товара №2 в корзине по ранее созданному имени добавленного товара
        System.out.println("[INFO] Поиск карточки товара по ранее созданному имени товара: " + name);
        cartItem = $$(".cart-item")
                .findBy(Condition.text(name));
        System.out.println("[INFO] Карточка товара №2 успешно найдена");

        // Проверка наименования добавленного товара №2 = наименованию ранее созданного товара
        System.out.println("[INFO] Поиск и проверка имени: " + name + " в карточке товара");
        cartItem.$("b").should(text(name));
        System.out.println("[INFO] Имя №2 соответствует: " + name);

        // Проверка цены добавленного товара №1
        System.out.println("[INFO] Поиск и проверка суммы товара в карточке товара: ожидаемая сумма = " + price);
        cartItem.$("div:nth-child(3)").should(partialText(String.valueOf(price)));
        System.out.println("[INFO] Cумма товара №2 соответствует: " + price);


        // Проверка суммы добавленных товаров №1 + №2
        System.out.println("[INFO] Поиск и проверка общей суммы товаров в корзине : ожидаемая сумма = " + (createdGoodPrice + price));
        double expectedCartItemTotal = createdGoodPrice + price;
        SelenideElement totalPrice = $x("//*[@id='total-price']");
                totalPrice.should(partialText(String.valueOf(expectedCartItemTotal)));
        System.out.println("[INFO] Cумма товаров соответствует: " + expectedCartItemTotal);


        System.out.println("\n=== Этап поиска товаров в корзине и выполнения проверок (SUCCESS) ===\n");


        System.out.println("\n=== Тест 3.2 завершён успешно ===\n");

    }


//==========================================================================================================================================

// 3.3. Войти в админку и добавить товар. Проверить уведомление после добавления товара.

    @Test
    @Tag("UI")
    @Order(3)
    void addGoodThrowAdminAndCheckNotificationTest() {
        System.out.println("\n=== Тест 3.3: Войти в админку и добавить товар. Проверить уведомление после добавления товара. ===\n");

        //вход в админку
        System.out.println("\n=== Этап входа в админку ===\n");
        System.out.println("[INFO] Переход на страницу админки");
        SelenideElement enterAdmin = $("[href='/admin']");
        enterAdmin.click();

        SelenideElement inputUsername = $x("//*[@id = 'username']");
        System.out.println("[INFO] Ввод валидного логина");
        inputUsername.sendKeys("admin");

        SelenideElement inputPass = $x("//*[@id = 'password']");
        System.out.println("[INFO] Ввод валидного пароля");
        inputPass.sendKeys("secret123");

        System.out.println("[INFO] Нажатие кнопки входа");
        SelenideElement pushButton = $x("//*[@type='submit']");
        pushButton.click();

        System.out.println("\n=== Этап входа в админку (SUCCESS) ===\n");


        // Создаём товар с рандомными значениями имени и цены
        System.out.println("\n=== Этап создания товара с рандомными значениями имени и цены ===\n");
        String uiGoodName = "UI-" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("[INFO] Генерация случайного имени: " + uiGoodName);
        Double uiGoodPrice = random.nextDouble(1.0, 100.0);
        System.out.println("[INFO] Генерация случайной цены: " + uiGoodPrice);

        System.out.println("\n=== Этап создания товара с рандомными значениями имени и цены (SUCCESS) ===\n");


        //Добавление товара через админку
        System.out.println("\n=== Этап добавления товара через админку ===\n");

        SelenideElement addName = $x("//*[@id = 'n-name']");
        addName.click();
        System.out.println("[INFO] Ввод имени товара: " + uiGoodName);
        addName.sendKeys(uiGoodName);

        SelenideElement addPrice = $x("//*[@id = 'n-price']");
        addPrice.click();
        System.out.println("[INFO] Ввод цены товара: " + uiGoodPrice);
        addPrice.sendKeys(String.valueOf(uiGoodPrice));

        System.out.println("[INFO] Нажатие кнопки 'Создать'");
        SelenideElement createButton = $x("//*[@id = 'add-btn']");
        createButton.click();

        System.out.println("\n=== Этап добавления товара через админку (SUCCESS) ===\n");

        // проверка тоста об успешном добавлении товара
        System.out.println("[INFO] Ожидание появления тоста об успешном создании товара...");
        SelenideElement toastVisible = $x("//*[@id = 'toast-container']");
        toastVisible.should(Condition.visible, Duration.ofSeconds(1));
        toastVisible.should(Condition.text("Товар успешно добавлен!"));

        System.out.println("[INFO] Проверка тоста об успешном создании товара. Тост соответствует");

        // Получение id созданного через UI товара через API (GET - запрос), чтобы удалить в @AfterEach
        Response resp = given()
                .spec(BasicRQ)
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
        }

        System.out.println("\n=== Тест 3.3 завершён успешно ===\n");
    }

    //==========================================================================================================================================


//3.4. Войти в админку и отредактировать товар. Выйти на список товаров и проверить, что изменения применились.






    }













/*

Задача 3: написать автотесты для следующих кейсов:

3.1. Добавить три единицы товара в корзину и оплатить их (общая стоимость не должна превышать 300 рублей). Проверить уведомление об обработке заказа.

3.2. Добавить в корзину несколько разных товаров и проверить, что общая цена в корзине считается корректно.

3.3. Войти в админку и добавить товар. Проверить уведомление после добавления товара.

3.4. Войти в админку и отредактировать товар. Выйти на список товаров и проверить, что изменения применились.

3.5. Хотя бы в одном кейсе должны быть обращение к API для генерации тестовых данных.

3.6. Хотя бы в одном кейсе в блоке @AfterEach тестовые данные должны удаляться.


Критерии проверки

Задача 3:

3.1–3.4 *

(обязательные) В проекте имеется метод, автоматизирующий тестовый кейс — 3 б. / каждый

3.5 Хотя бы в одном кейсе реализована генерация тестовых данных через API — 3б.

3.6 Хотя бы в одном кейсе тестовые данные удаляются в блоке @AfterEach — 3б.

 */