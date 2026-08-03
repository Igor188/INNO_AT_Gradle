import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import java.util.Random;
import java.util.UUID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Lesson4HomeworkRestAssuredTask2 {

    public record Good (String name, Double price) {} //для запроса POST /goods/add с параметрами body

    private final Random random = new Random();

    private RequestSpecification BasicRQ = new RequestSpecBuilder()
            .setBaseUri("http://localhost:8080")
            .setAuth(RestAssured.preemptive().basic("admin", "secret123"))
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static int createdGoodId;




    // ================== 1. POST /goods/add ================================================
    //==================200==================================================================
    @Test
    @Tag("api")
    @Order(1)
    void addGood_valid_shouldReturn200or201() {
        // Создаём товар с рандомными значениями имени и цены
        String name = "Good-" + UUID.randomUUID().toString().substring(0, 8);
        Double price = random.nextDouble(1.0, 100.0);
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


        createdGoodId = response.jsonPath().getInt("data.id"); // Сохраним ID для будущих тестов (если приходит в ответе)

    }
    //==================400==================================================================
    @Test
    @Tag("api")
    @Order(2)
    void addGood_negativePrice_shouldReturn400() {
        // Создаём товар с рандомными значениями имени и цены
        String name = "Good-" + UUID.randomUUID().toString().substring(0, 8);
        Double price = random.nextDouble(1.0, 100.0)-100.0;//в цене товара отнимаем 100.0
        given()
                .spec(BasicRQ)
                .body(new Good(name, price))
                .when()
                .post("/goods/add")
                .then()
                .log().all()
                .statusCode(400);
    }







// ================== 2. GET /goods/{id} ================================================
//==================200==================================================================
@Test
@Tag("api")
@Order(3)
void getGoodById_shouldReturn200() {
    given()
            .spec(BasicRQ)
            .pathParam("id", createdGoodId)
            .when()
            .get("/goods/{id}")
            .then()
            .log().all()
            .statusCode(200);
}

//==================404==================================================================

    @Test
    @Tag("api")
    @Order(4)
    void getGoodById_nonCreated_shouldReturn404() {
        Integer notcreatedid = random.nextInt(900,1000);// Сгенерируем id товара которого нет в БД
        given()
                .spec(BasicRQ)
                .pathParam("id", notcreatedid)
                .when()
                .get("/goods/{id}")
                .then()
                .log().all()
                .statusCode(404);
    }









// ================== 3. GET /goods/list ================================================
//==================200==================================================================
    @Test
    @Tag("api")
    @Order(5)
    void getGoodsList_shouldReturn200(){
        given()
                .spec(BasicRQ)
                .queryParam("page", 0)
                .queryParam("size", 1000)
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200);
    }









// ================== 4. PATCH /goods/{id} ================================================
//==================200====================================================================
    @Test
    @Tag("api")
    @Order(6)
    void patchGood_priceOnly_shouldReturn200() {
        // Создаём товар с рандомными значениями имени и цены
        String name = "Good-" + UUID.randomUUID().toString().substring(0, 8);
        Double price = random.nextDouble(1.0, 100.0);
        given()
                .spec(BasicRQ)
                .pathParam("id", createdGoodId)
                .body(new Good(name, price))
                .when()
                .patch("/goods/{id}")
                .then()
                .log().all()
                .statusCode(200);
    }

    //==================400====================================================================
    @Test
    @Tag("api")
    @Order(7)
    void patchGood_invalidPrice_shouldReturn400() {
        // Создаём товар с рандомными значениями имени и цены
        String name = "Good-" + UUID.randomUUID().toString().substring(0, 8);
        Double price = random.nextDouble(1.0, 100.0)-100.0;//в цене товара отнимаем 100.0
        given()
                .spec(BasicRQ)
                .pathParam("id", createdGoodId)
                .body(new Good(name, price))
                .when()
                .patch("/goods/{id}")
                .then()
                .log().all()
                .statusCode(400);
    }

//==================404====================================================================
    @Test
    @Tag("api")
    @Order(8)
    void patchGood_nonCreated_shouldReturn404() {
        Integer notcreatedid = random.nextInt(900,1000);// Сгенерируем id товара которого нет в БД
        // Создаём товар с рандомными значениями имени и цены
        String name = "Good-" + UUID.randomUUID().toString().substring(0, 8);
        Double price = random.nextDouble(1.0, 100.0);
        given()
                .spec(BasicRQ)
                .pathParam("id", notcreatedid)
                .body(new Good(name, price))
                .when()
                .patch("/goods/{id}")
                .then()
                .log().all()
                .statusCode(404);
    }








// ================== 5. DELETE /goods/{id} ================================================
//==================200====================================================================
    @Test
    @Tag("api")
    @Order(9)
    void deleteGood_created_shouldReturn200or204() {
        given()
                .spec(BasicRQ)
                .pathParam("id", createdGoodId)
                .when()
                .delete("/goods/{id}")
                .then()
                .log().all()
                .statusCode(anyOf(is(200), is(204)));   // принимает 200 ИЛИ 204 код
    }


    //==================404====================================================================
    @Test
    @Tag("api")
    @Order(10)
    void deleteGood_nonCreated_shouldReturn404() {
        Integer notcreatedid = random.nextInt(900,1000);// Сгенерируем id товара которого нет в БД
        given()
                .spec(BasicRQ)
                .pathParam("id", notcreatedid) // Или можно использовтаь createdGoodId (она уже удалена)
                .when()
                .delete("/goods/{id}")
                .then()
                .log().all()
                .statusCode(404);
    }

}





