import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Lesson4HomeworkRestAssuredTask1 {

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
    @Order(1)
    void RATest_status_empty_body(){
        given()
                .baseUri("http://localhost:8080")
                .log().all()
                .queryParam("page", 0)
                .queryParam("size", 1000)
                .auth() //аутентификация в Сваггер
                .basic("admin", "secret123") //креды для аутентификации
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
    @Order(2)
    void RATest_status_empty_body_spec(){
        given()
                .spec(BasicRQ)
                .queryParam("page", 0)
                .queryParam("size", 1000)
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
    @Order(3)
    void RATest_Create_Load_Goods(){
        // Создаём товар с рандомными значениями имени и цены
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
                .queryParam("size", 1000)
                .when()
                .get("/goods/list")
                .then()
                .log().all()
                .statusCode(200)
                .body("goods.name", hasItem(name));
    }

    // ================== Задача 1.4: POST /goods/add + проверка через AssertJ ==================
    @Test
    @Tag("api")
    @Order(4)
    void RATest_Create_Load_Goods_AssertJ(){
        // Создаём товар с рандомными значениями имени и цены
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


        // Проверяем, что товар появился в списке (AssertJ)
        Response response = given() //через объект response
                .spec(BasicRQ)
                .queryParam("page", 0)
                .queryParam("size", 1000)
                .when()
                .get("/goods/list");
        //        2 вариант как можно response вытащить
        //        List<String> actual_result_names = response.path("goods.name");
        //        int actual_result_code = response.getStatusCode();

       // Проверка через AssertJ
        //       1 (итоговый) вариант как можно response вытащить
        assertThat(response.statusCode())
                .as("Код ответа должен быть 200")
                .isEqualTo(200);

        List<String> names = response.jsonPath().getList("goods.name");
        assertThat(names)
                .as("Список имён товаров должен содержать созданное имя товара", name)
                .contains(name);


        //       2 вариант как можно response вытащить - продолжение
        // assertThat(actual_result_code)
           //     .as("Код ответа должен быть 200")
             //   .isEqualTo(200);

        //assertThat(actual_result_names)
         //       .as("Список имён товаров должен содержать созданное имя товара", name)
         //       .contains(name);

      }

}





