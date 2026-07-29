import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class Lesson3Homework_Assert_Task2 {

// ================== Циклы ==================
    @Tag("cycle")
    @RepeatedTest(10)
    @Disabled
    void TestBlastOff() {
        Lesson1Homework_Cycle obj = new Lesson1Homework_Cycle();
        String actual_result = obj.blastOff(5);
        String expected_result = "5 4 3 2 1 Поехали!";
        assertThat(actual_result)
                .as("Проверка blastOff(5): ожидалась строка '%s'", expected_result)
                .isEqualTo(expected_result);
    }


    @Tag("cycle")
    @RepeatedTest(10)
    @Disabled
    void testHasBugTrue() {
        Lesson1Homework_Cycle obj = new Lesson1Homework_Cycle();
        boolean actual_result = obj.hasBug(new String[]{"Info", "Bug", "Warn"});
        assertThat(actual_result)
                .as("Массив содержит 'Bug' → метод должен вернуть true")
                .isTrue();
    }

    @Tag("cycle")
    @RepeatedTest(10)
    @Disabled
    void testSumToN() {
        Lesson1Homework_Cycle obj = new Lesson1Homework_Cycle();
        int n = 5;
        int actual_result = obj.sumToN(n);
        int expected_result = 15; // 1+2+3+4+5
        assertThat(actual_result)
                .as("Сумма чисел от 1 до %d должна быть %d", n, expected_result)
                .isEqualTo(expected_result);
    }

    @Tag("cycle")
    @RepeatedTest(10)
    @Disabled
    void testGetEvenInRange() {
        Lesson1Homework_Cycle obj = new Lesson1Homework_Cycle();
        String actual_result = obj.getEvenInRange(1, 10);
        String expected_result = "2 4 6 8 10 ";
        assertThat(actual_result)
                .as("Чётные числа в интервале [1,10] : ")
                .isEqualTo(expected_result);
    }



}
