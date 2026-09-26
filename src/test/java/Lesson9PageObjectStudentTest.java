import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class Lesson9PageObjectStudentTest {
    @Test
    void studentTest() {
        given()
                .body(Lesson9PageObjectStudent.builder()
                        .setName("John")
                        .setSecondName("Jackson")
                        .build())
                .post();
    }
}
