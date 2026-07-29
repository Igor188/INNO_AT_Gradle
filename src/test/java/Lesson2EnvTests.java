import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class Lesson2EnvTests {
    @Test
    @Tag("Smoke")
    void envTest1(){
        System.out.println("test circuit: " + System.getProperty("CIRCUIT"));
      //  String testLogin = (System.getenv("TEST_LOGIN") != null)? System.getenv("TEST_LOGIN"): "ADMIN";
      //  System.out.println("test account: " + System.getenv("TEST_LOGIN"));
    }
}

