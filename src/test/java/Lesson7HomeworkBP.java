import com.codeborne.selenide.Condition;
import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.SelenideElement;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.anyOf;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Lesson7HomeworkBP {

    private static final Logger logger = Logger.getLogger(Lesson7HomeworkBP.class.getName());

    private final Random random = new Random();

    public record Good(String name, Double price) {
    } //для запроса POST /goods/add с параметрами body

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

    @BeforeEach
    void setUp() {
        // Создаём товар с рандомными значениями имени и цены
        logger.log(Level.INFO, "=== Начало подготовки тестовых данных ===");
        String name = "Good-" + UUID.randomUUID().toString().substring(0, 8);
        Double price = random.nextDouble(1.0, 99.0);
        logger.log(Level.INFO, "Создание товара через API: имя = {0}, цена = {1}", new Object[]{name, price});
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


        createdGoodName = name; // Сохраним имя для будущих ассертов
        createdGoodPrice = price; // Сохраним цену для будущих ассертов
        createdGoodId = response.jsonPath().getInt("data.id"); // Сохраним ID для будущих тестов и ассертов
        logger.log(Level.INFO, "Товар создан, имя: {0}, id: {1}", new Object[]{createdGoodName, createdGoodId});// Сохраним ID для будущих тестов

        logger.log(Level.INFO, "Test method start\n" +
                "=================================================\n" +
                "Запуск браузера и открытие сайта\n");
        open("http://localhost:8080/");
        logger.log(Level.INFO, "=== Подготовка завершена ===");
    }


    @AfterEach
    void cleanUp() {
        logger.log(Level.INFO, "=== Начало очистки памяти и закрытия браузера ===");
            logger.log(Level.INFO, "Удаление товара с id = {0}", createdGoodId);
            given()
                    .spec(BasicRQ)
                    .pathParam("id", createdGoodId)
                    .when()
                    .delete("/goods/{id}")
                    .then()
                    .log().all()
                    .statusCode(anyOf(is(200), is(204)))   // принимает 200 ИЛИ 204 код
                    .body(containsString("deleted successfully"));
            logger.log(Level.INFO, "Ранее сгенерированный товар с именем: " + createdGoodName + " и " + "id товара: " + createdGoodId + " удалён успешно");

            logger.log(Level.INFO, "Test method end\n" +
                "==================================\n" +
                "Закрытие браузера и очистка памяти\n");
        closeWebDriver();
        logger.log(Level.INFO, "=== Очистка памяти и закрытия браузера завершено ===");
}


    //==========================================================================================================================================

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

}





/*

Задача 1: написать 2 автотеста для кейсов:

1.1 Перетащить элемент в корзину с помощью Drag-and-Drop.

1.2 Удалить добавленный элемент из корзины и проверить, что он там больше не отображается.

Формат результата

Создать публичный репозиторий на GitHub и приложить ссылку на коммит.

Критерии проверки

Задача 1:

В проекте имеется метод, автоматизирующий тестовый кейс — 3 б. / каждый

*/