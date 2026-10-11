import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.*;

public class Lesson10AllureHomeworkApiAssert {

    private final Response response;

    public Lesson10AllureHomeworkApiAssert(Response response) {
        this.response = response;
    }

    @Step("Проверить: статус-код входит в допустимые [{expected}]")
    public Lesson10AllureHomeworkApiAssert statusCode(Matcher<Integer> expected) {
        response.then().statusCode(expected);
        return this;
    }

    @Step("Проверить: статус-код = {expected}")
    public Lesson10AllureHomeworkApiAssert statusCode(int expected) {
        response.then().statusCode(expected);
        return this;
    }

    @Step("Проверить: поле '{path}' = '{expectedValue}'")
    public Lesson10AllureHomeworkApiAssert bodyEquals(String path, Object expectedValue) {
        response.then().body(path, equalTo(expectedValue));
        return this;
    }

    @Step("Проверить: тело ответа содержит '{substring}'")
    public Lesson10AllureHomeworkApiAssert bodyContains(String path, String substring) {
        if (path == null || path.isEmpty()) {
            // Проверка всего тела как строки (для text/plain ответов)
            response.then().body(org.hamcrest.Matchers.containsString(substring));
        } else {
            response.then().body(path, org.hamcrest.Matchers.containsString(substring));
        }
        return this;
    }

    @Step("Проверить: поле '{path}' содержит элемент '{item}'")
    public Lesson10AllureHomeworkApiAssert bodyHasItem(String path, Object item) {
        response.then().body(path, hasItem(item));
        return this;
    }
}