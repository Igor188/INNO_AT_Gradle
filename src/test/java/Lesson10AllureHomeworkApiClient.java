import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class Lesson10AllureHomeworkApiClient {

    public record Good (String name, Double price) {} //для запроса POST /goods/add с параметрами body

    private final RequestSpecification spec;

    public Lesson10AllureHomeworkApiClient(RequestSpecification spec) {
        this.spec = spec;
    }

    @Step("POST /goods/add — создать товар: name={name}, price={price}")
    public Response addGood(String name, double price) {
        return given().spec(spec)
                .body(new Good(name, price))
                .when().post("/goods/add");
    }

    @Step("GET /goods/{id} — получить товар id={id}")
    public Response getGoodById(int id) {
        return given().spec(spec).pathParam("id", id).when().get("/goods/{id}");
    }

    @Step("GET /goods1/{id} — получить товар по несуществующему id={id}")
    public Response getGoodById_nonCreated(int id) {
        return given().spec(spec).pathParam("id", id).when().get("/goods1/{id}");
    }

    @Step("GET /goods/list — список товаров (page={page}, size={size})")
    public Response getGoodsList(int page, int size) {
        return given().spec(spec)
                .queryParam("page", page).queryParam("size", size)
                .when().get("/goods/list");
    }

    @Step("PATCH /goods/{id} — обновить товар id={id}: name={name}, price={price}")
    public Response patchGood(int id, String name, double price) {
        return given().spec(spec).pathParam("id", id)
                .body(new Good(name, price))
                .when().patch("/goods/{id}");
    }

    @Step("DELETE /goods/{id} — удалить товар id={id}")
    public Response deleteGood(int id) {
        return given().spec(spec).pathParam("id", id).when().delete("/goods/{id}");
    }
}





