import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import java.util.Random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class Lesson3AssertJ {

    @RepeatedTest((100))
    public void test() {
        Random random = new Random();
        Integer actualResult = random.nextInt(1, 10);
        Integer expectedBound = 5;

        // Этот блок сейчас не нужен, но если раскомментировать, то скобки должны быть парными
        // for (int i = 0; i < 10; i++) {
        //     actualResult = actualResult + random.nextInt(20, 70);
        // }

                assertThat(actualResult)
                .as("Check that actual result is less than %d and other conditions", expectedBound)
                .isLessThan(expectedBound)
                .isNotEqualTo(3)
                .isOdd();
    }
}
