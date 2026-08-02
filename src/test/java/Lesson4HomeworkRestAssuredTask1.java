import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import java.util.Random;
import java.util.UUID;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;


public class Lesson4HomeworkRestAssuredTask1 {

    public record Request1 (String foo, Integer foo2){} //для запроса GET /goods/list с query-параметрами

    public record Good (String name, Double price) {} //для запроса POST /goods/add с параметрами body

    private final Random random = new Random();

    private RequestSpecification BasicRQ = new RequestSpecBuilder()
            .setBaseUri("http://localhost:8080")
            .setAuth(RestAssured.preemptive().basic("admin", "secret123"))
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    // ================== Задача 1.1: given().when().then() ======================================
    @Test
    @Tag("api")
    @Disabled
    void RATest_status_empty_body(){
        given()
                .baseUri("http://localhost:8080")
                .log().all()
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200)
                .body("goods",hasSize(0));
    }


// ================== Задача 1.2: RequestSpecification ==========================================
    @Test
    @Tag("api")
    @Disabled
    void RATest_status_empty_body_spec(){
        given()
                .spec(BasicRQ)
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200)
                .body("goods",hasSize(0));
    }

// ================== Задача 1.3: POST /goods/add + проверка через REST Assured ==================
    @Test
    @Tag("api")
    void RATest_Create_Load_Goods(){
        // Создаём товар
        String name = "Good-" + UUID.randomUUID().toString().substring(0, 8);
        Double price = random.nextDouble(1.0,100.0);
        given()
                .spec(BasicRQ)
                .body(new Good(name, price))
                .when()
                .post("/goods/add")
                .then()
                .log().all()
                .statusCode(anyOf(is(200), is(201)));   // принимает 200 ИЛИ 201 код


        // Проверяем, что товар появился в списке (REST Assured)
        given()
                .spec(BasicRQ)
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200)
                .body("goods.name", hasItem(name));
    }






}







/*
given() - стадия подготовки (Setup), где мы описываем всё, что отправляем на сервер
given()
                .baseUri("https://api.example.com")
                .basePath("/v1")
                .header("Accept", "application/json")
                .queryParam("limit", 10)
                .log().all(); // Видим запрос в консоли

when() - переводит тест из состояния подгтовки в состояние действия
get ("/users/{id}"), те же методы REST - GET/POST/PUT/PATCH/DELETE

then() - проверка соответствует ли реальный ответ нашим ожиданиям

 */