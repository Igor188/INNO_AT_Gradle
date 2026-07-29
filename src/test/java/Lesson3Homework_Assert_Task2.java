import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class Lesson3Homework_Assert_Task2 {

// ================== Циклы ==================
    @Tag("cycle")
    @RepeatedTest(10)
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
    void testHasBugTrue() {
        Lesson1Homework_Cycle obj = new Lesson1Homework_Cycle();
        boolean actual_result = obj.hasBug(new String[]{"Info", "Bug", "Warn"});
        assertThat(actual_result)
                .as("Массив содержит 'Bug' → метод должен вернуть true")
                .isTrue();
    }

    @Tag("cycle")
    @RepeatedTest(10)
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
    void testGetEvenInRange() {
        Lesson1Homework_Cycle obj = new Lesson1Homework_Cycle();
        String actual_result = obj.getEvenInRange(1, 10);
        String expected_result = "2 4 6 8 10 ";
        assertThat(actual_result)
                .as("Чётные числа в интервале [1,10] : ")
                .isEqualTo(expected_result);
    }

// ================== Логика и тернарный оператор ==================
    @Tag("logic")
    @RepeatedTest(10)
    void testIsEven() {
        int n = 4;
        boolean actual_result = Lesson1Homework_Logic_ternarii.isEven(n);
        assertThat(actual_result)
                .as("Должно вернуть true, если в методе isEven введено четное число: введено число %d:", n)
                .isTrue();
    }


    @Tag("logic")
    @RepeatedTest(10)
    void testIsPositive() {
        int n = -2;
        boolean actual_result = Lesson1Homework_Logic_ternarii.isPositive(n);
        assertThat(actual_result)
                .as("Должно вернуть false, если в методе isPositive введено отрицательное число: введено число %d:", n)
                .isFalse();
    }

    @Tag("logic")
    @RepeatedTest(10)
    void testCheckAccessAllowed() {
        int age = 20;
        String actual_result = Lesson1Homework_Logic_ternarii.checkAccess(age);
        assertThat(actual_result)
                .as("Должно вернуть 'Allowed', если в методе checkAccess введено значение > 18: введено число %d:", age)
                .isEqualTo("Allowed");
    }

    @Tag("logic")
    @RepeatedTest(10)
    void testGetGradeA() {
        int score = 85;
        String actual_result = Lesson1Homework_Logic_ternarii.getGrade(score);
        assertThat(actual_result)
                .as("Должно вернуть '81-100: A', если в методе getGrade введено значение согласно градации от 81-100: введено число %d:", score)
                .isEqualTo("81-100: A");
    }

// ================== Массивы и списки ==================
    @Tag("arrays")
    @RepeatedTest(10)
    void testFindMax() {
        Lesson1Homework_Arrays_ArraysList1 obj = new Lesson1Homework_Arrays_ArraysList1();
        int actual_result = obj.findMax(new int[]{3, 7, 2, 9, 1, 5});
        assertThat(actual_result)
                .as("Максимум в массиве {3,7,2,9,1,5} должен быть 9")
                .isEqualTo(9);
    }

    @Tag("arrays")
    @RepeatedTest(10)
    void testReverse() {
        Lesson1Homework_Arrays_ArraysList2 obj = new Lesson1Homework_Arrays_ArraysList2();
        String[] actual_result = obj.reverse(new String[]{"Яблоко", "Груша", "Слива", "Апельсин"});
        String[] expected_result = {"Апельсин", "Слива", "Груша", "Яблоко"};
        assertThat(actual_result)
                .as("Массив фруктов должен быть развёрнут задом наперёд")
                .containsExactly(expected_result);
    }

    @Tag("arrays")
    @RepeatedTest(10)
    void testCalcAverage() {
        List<Integer> list = List.of(10, 20, 30, 40, 50);
        int actual_result = Lesson1Homework_Arrays_ArraysList3.calcAverage(list);
        int expected_result = 30;
        assertThat(actual_result)
                .as("Среднее арифметическое списка 10,20,30,40,50 должно быть 30: ")
                .isEqualTo(expected_result);
    }

    @Tag("arrays")
    @RepeatedTest(10)
    void testRemoveSpecificName() {
        Lesson1Homework_Arrays_ArraysList4 obj = new Lesson1Homework_Arrays_ArraysList4();
        List<String> actual_result = obj.removeSpecificName(List.of("Анна", "Борис", "Анна", "Виктор"), "Анна");
        List<String> expected_result = List.of("Борис", "Виктор");
        assertThat(actual_result)
                .as("После удаления 'Анна' из списка должен остаться [Борис, Виктор]")
                .isEqualTo(expected_result);
    }
}
