import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class Lesson4RestAssuredTest {
    @Test
    void raTest(){
        given()
                .baseUri("https://api.example.com")
                .basePath("/v1")
                .header("Accept", "application/json")
                .queryParam("limit", 10)
                .log().all(); // Видим запрос в консоли
    }
}







/*
given() - стадия подготовки (Setup), где мы описываем всё, что отправляем на сервер
when()
then()
 */