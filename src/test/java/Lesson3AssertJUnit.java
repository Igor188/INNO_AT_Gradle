import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class Lesson3AssertJUnit {
    @Test

    public void test(){
        Integer dig = new Random().nextInt(1,10);
        Integer expectedResult = 3;

      //assert dig.equals(expectedResult);
      //Assertions.assertEquals(expectedResult, dig);
      //  assertArrayEquals(); можно вызвать отдельно метод библиотеки если осуществить импорт её
    }
}
