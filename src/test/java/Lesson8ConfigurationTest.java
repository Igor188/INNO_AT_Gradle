import com.codeborne.selenide.*;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.v146.input.Input;
import io.restassured.builder.RequestSpecBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Lesson8ConfigurationTest {

    @Test
    @Tag("UI")
    @Order(1)
    void propsTest() {
        Properties props = new Properties();
        InputStream propsStream;
        propsStream = getClass().getResourceAsStream("config.properties");


        try {
            props.load(propsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String url = props.getProperty("URL");

        given()
                .get(url)
                .then()
                .log().all();

        System.out.println(props.getProperty("URL"));
        System.out.println(props.getProperty("MODE"));
        System.out.println(props.getProperty("GOOD_NAME"));
    }


    @Test
    @Tag("UI")
    @Order(2)
    void propsTest2() {
    /*new RestApiBuilder(new ConfigProvider().getProperty("URL"))
        .getSpec()
        .log().all()
        .get()
        .then()
        .log().all();
     */
    }


    @Test
    @Tag("UI")
    @Order(3)
    void Aeonbits() {

        /*ApiConfig config = ConfigFactory.create(ApiConfig.class);

        new RestApiBuilder(config.url())
                .getSpec
                .log().all()
                .get()
                .then()
                .log().all();

*/
  /*      new RestApiBuilder(ConfigProvider.apiProps.url())
                .getSpec
                .log().all()
                .get()
                .then()
                .log().all();

   */
    }




}