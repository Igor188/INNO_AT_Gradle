import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class Lesson4RestAssuredSpecAuth {

    public record Request1 (String foo, Integer foo2){} //для запроса GET /goods/list с query-параметрами

    public record Good (String name, Double price) {} //для запроса POST /goods/add с параметрами body

    private RequestSpecification basicRQ = new RequestSpecBuilder()
        .setBaseUri("http://localhost:8080")
        .log(LogDetail.ALL)
        .addQueryParam("page", 0)
        .build();



    @Test
    void raTest(){
        given()
                .spec(basicRQ)
                .queryParam("size", 1)
                .auth() //аутентификация в Сваггер
                .basic("admin", "secret123") //креды для аутентификации
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200);
    }


    @Test
    void raTest2(){
        given()
                .spec(basicRQ)
                .queryParam("size", 2)
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200);
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
get ("/users/{id}")
те же методы REST - GET/POST/PUT/PATCH/DELETE

then() - проверка соответствует ли реальный ответ нашим ожиданиям

 */