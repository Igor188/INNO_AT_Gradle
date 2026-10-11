import Config.ApiConfig;
import Config.ConfigProvider;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.hamcrest.Matchers.*;
import io.restassured.parsing.Parser;

@Epic("INNO-AT")
@Feature("API Allure")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Lesson10AllureHomeworkRestTest {

    private static final Logger logger = Logger.getLogger(Lesson10AllureHomeworkRestTest.class.getName());
    private static final ApiConfig config = ConfigProvider.apiProps;

    private final Random random = new Random();

    private static RequestSpecification basicRQ;
    private static Lesson10AllureHomeworkApiClient api;

    private static int createdGoodId;
    private static String createdGoodName;
    private static Double createdGoodPrice;

    @BeforeAll
    static void setUpAllure() {
        // Регистрируем парсер для text/plain — сервер отвечает plain text на DELETE
        RestAssured.registerParser("text/plain", Parser.TEXT);

        logger.log(Level.INFO, "=================================================");
        logger.log(Level.INFO, "Запуск API-автотестов с параметрами:");
        logger.log(Level.INFO, "API_URL:     {0}", config.apiUrl());
        logger.log(Level.INFO, "TIMEOUT:     {0} ms", config.timeout());
        logger.log(Level.INFO, "GOOD_NAME:   {0}", config.goodName());
        logger.log(Level.INFO, "GOOD_PRICE:  {0}", String.format(Locale.ROOT, "%.2f", config.goodPrice()));
        logger.log(Level.INFO, "=================================================");

        basicRQ = new RequestSpecBuilder()
                .setBaseUri(config.apiUrl())
                .setAuth(RestAssured.preemptive().basic(config.adminLogin(), config.adminPassword()))
                .setContentType(ContentType.JSON)
                .addFilter(new io.qameta.allure.restassured.AllureRestAssured())
                .log(LogDetail.ALL)
                .build();

        api = new Lesson10AllureHomeworkApiClient(basicRQ);
    }

    // ================== 1. POST /goods/add ================================================
    //==================200==================================================================
    @Test
    @Tag("api")
    @Order(1)
    @Story("POST /goods/add — создание товара")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Создаём товар с валидными именем и ценой, проверяем 200/201 и message=success")
    void addGood_valid_shouldReturn200or201() {
        // Создаём товар с рандомными значениями имени и цены
        String name = "Good-" + UUID.randomUUID().toString().substring(0, 8);
        Double price = random.nextDouble(1.0, 100.0);
        Response response = api.addGood(name, price);
        new Lesson10AllureHomeworkApiAssert(response)
                .statusCode(anyOf(is(200), is(201)))
                .bodyEquals("message", "success");


        createdGoodId = response.jsonPath().getInt("data.id"); // Сохраним ID для будущих тестов (если приходит в ответе)
        createdGoodName = name; // Сохраним имя для будущих ассертов
        createdGoodPrice = price; // Сохраним цену для будущих ассертов

        logger.log(Level.INFO, "Товар создан: name={0}, id={1}",
                new Object[]{createdGoodName, createdGoodId});
    }
    //==================400==================================================================
    @Test
    @Tag("api")
    @Order(2)
    @Story("POST /goods/add — валидация отрицательной цены")
    @Severity(SeverityLevel.NORMAL)
    @Description("Отправляем отрицательную цену → ожидаем 400 и сообщение о валидации")
    void addGood_negativePrice_shouldReturn400() {
        // Создаём товар с рандомными значениями имени и цены
        String name = "Good-" + UUID.randomUUID().toString().substring(0, 8);
        Double price = random.nextDouble(1.0, 100.0)-100.0;//в цене товара отнимаем 100.0
        Response response = api.addGood(name, price);
        new Lesson10AllureHomeworkApiAssert(response)
                .statusCode(400)
                .bodyEquals("price", "Price can not be less, than 0");
    }


// ================== 2. GET /goods/{id} ================================================
//==================200==================================================================
@Test
@Tag("api")
@Order(3)
@Story("GET /goods/{id} — существующий товар")
@Severity(SeverityLevel.CRITICAL)
void getGoodById_shouldReturn200() {
    Response response = api.getGoodById(createdGoodId);
    new Lesson10AllureHomeworkApiAssert(response)
            .statusCode(200)
            .bodyEquals("id", createdGoodId)
            .bodyEquals("name", createdGoodName)
            .bodyEquals("price", createdGoodPrice.floatValue());
}

//==================404==================================================================

    @Test
    @Tag("api")
    @Order(4)
    @Story("GET /goods1/{id} — несуществующий товар")
    @Severity(SeverityLevel.MINOR)
    void getGoodById_nonCreated_shouldReturn404() {
        int notCreatedId = random.nextInt(900,1000);// Сгенерируем id товара которого нет в БД
        Response response = api.getGoodById_nonCreated(notCreatedId);
        new Lesson10AllureHomeworkApiAssert(response)
                .statusCode(404)
                .bodyEquals("error", "Not Found");
    }


// ================== 3. GET /goods/list ================================================
//==================200==================================================================
    @Test
    @Tag("api")
    @Order(5)
    @Story("GET /goods/list — содержит созданный товар")
    @Severity(SeverityLevel.CRITICAL)
    void getGoodsList_shouldReturn200(){
        Response response = api.getGoodsList(0, 1000);
        new Lesson10AllureHomeworkApiAssert(response)
                .statusCode(200)
                .bodyHasItem("goods.id", createdGoodId)
                .bodyHasItem("goods.name", createdGoodName)
                .bodyHasItem("goods.price", createdGoodPrice.floatValue());
    }


// ================== 4. PATCH /goods/{id} ================================================
//==================200====================================================================
    @Test
    @Tag("api")
    @Story("PATCH /goods/{id} — успешное обновление")
    @Severity(SeverityLevel.CRITICAL)
    @Order(6)
    void patchGood_priceOnly_shouldReturn200() {
        // Создаём товар с рандомными значениями имени и цены
        String newName = "Good-" + UUID.randomUUID().toString().substring(0, 8);
        Double newPrice = random.nextDouble(1.0, 100.0);
        Response response = api.patchGood(createdGoodId, newName, newPrice);
        new Lesson10AllureHomeworkApiAssert(response)
                .statusCode(200)
                .bodyEquals("id", createdGoodId)
                .bodyEquals("name", newName)
                .bodyEquals("price", newPrice.floatValue());

        createdGoodName = newName; // Сохраним обновленное имя для будущих ассертов
        createdGoodPrice = newPrice; // Сохраним обновленную цену для будущих ассертов
    }

    //==================400====================================================================
    @Test
    @Tag("api")
    @Order(7)
    @Story("PATCH /goods/{id} — валидация цены")
    @Severity(SeverityLevel.NORMAL)
    void patchGood_invalidPrice_shouldReturn400() {
        // Создаём товар с рандомными значениями имени и цены
        String name = "Good-" + UUID.randomUUID().toString().substring(0, 8);
        Double price = random.nextDouble(1.0, 100.0)-100.0;//в цене товара отнимаем 100.0
        Response response = api.patchGood(createdGoodId, "Some", -50.0);
        new Lesson10AllureHomeworkApiAssert(response)
                .statusCode(400)
                .bodyEquals("price", "Price can not be less, than 0");
    }

//==================404====================================================================
    @Test
    @Tag("api")
    @Order(8)
    @Story("PATCH /goods/{id} — несуществующий товар")
    @Severity(SeverityLevel.MINOR)
    void patchGood_nonCreated_shouldReturn404() {
        Integer notcreatedid = random.nextInt(900,1000);// Сгенерируем id товара которого нет в БД
        // Создаём товар с рандомными значениями имени и цены
        String name = "Good-" + UUID.randomUUID().toString().substring(0, 8);
        int notCreatedId = random.nextInt(900, 1000);
        Response response = api.patchGood(notCreatedId, "Some", 10.0);
        new Lesson10AllureHomeworkApiAssert(response)
                .statusCode(404)
                .bodyContains("", "not found");
    }


// ================== 5. DELETE /goods/{id} ================================================
//==================200====================================================================
    @Test
    @Tag("api")
    @Order(9)
    @Story("DELETE /goods/{id} — успешное удаление")
    @Severity(SeverityLevel.CRITICAL)
    void deleteGood_created_shouldReturn200or204() {
        Response response = api.deleteGood(createdGoodId);
        new Lesson10AllureHomeworkApiAssert(response)
                .statusCode(anyOf(is(200), is(204)))
                .bodyContains("", "deleted successfully");
    }


    //==================404====================================================================
    @Test
    @Tag("api")
    @Order(10)
    @Story("DELETE /goods/{id} — несуществующий товар")
    @Severity(SeverityLevel.MINOR)
    void deleteGood_nonCreated_shouldReturn404() {
        int notCreatedId = random.nextInt(900, 1000);// Сгенерируем id товара которого нет в БД
        Response response = api.deleteGood(notCreatedId);
        new Lesson10AllureHomeworkApiAssert(response)
                .statusCode(404)
                .bodyContains("", "not found");
    }

}





