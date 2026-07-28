import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class Lesson2HomeworkCalledMethods {
    // вспомогательный метод для проверки и вывода результата
    private void check(boolean condition, String testName) {
        if (condition) {
            System.out.println(testName + ": TEST PASSED");
        } else {
            System.out.println(testName + ": TEST FAILED");
            throw new RuntimeException(testName + " FAILED");
        }
    }

    // ---------- Урок 1. Циклы ----------
    @Test
    void testBlastOff() {
        Lesson1Homework_Cycle obj = new Lesson1Homework_Cycle();
        String actual = obj.blastOff(5);
        String expected = "5 4 3 2 1 Поехали!";
        check(expected.equals(actual), "blastOff(5)");
    }

    @Test
    void testHasBugTrue() {
        Lesson1Homework_Cycle obj = new Lesson1Homework_Cycle();
        boolean actual = obj.hasBug(new String[]{"Info", "Bug", "Warn"});
        check(actual == true, "hasBug с массивом, содержащим Bug");
    }

    @Test
    void testSumToN() {
        Lesson1Homework_Cycle obj = new Lesson1Homework_Cycle();
        int actual = obj.sumToN(5);
        int expected = 15; // 1+2+3+4+5
        check(expected == actual, "sumToN(5)");
    }

    @Test
    void testGetEvenInRange() {
        Lesson1Homework_Cycle obj = new Lesson1Homework_Cycle();
        String actual = obj.getEvenInRange(1, 10);
        String expected = "2 4 6 8 10 ";
        check(expected.equals(actual), "getEvenInRange(1,10)");
    }

    // ---------- Урок 1. Логика и тернарный оператор ----------
    @Test
    void testIsEven() {
        boolean actual = Lesson1Homework_Logic_ternarii.isEven(4);
        check(actual == true, "isEven(4)");
    }

    @Test
    void testIsPositive() {
        boolean actual = Lesson1Homework_Logic_ternarii.isPositive(-2);
        check(actual == false, "isPositive(-2)");
    }

    @Test
    void testCheckAccessAllowed() {
        String actual = Lesson1Homework_Logic_ternarii.checkAccess(20);
        check(actual.equals("Allowed"), "checkAccess(20)");
    }

    @Test
    void testGetGradeA() {
        String actual = Lesson1Homework_Logic_ternarii.getGrade(85);
        check(actual.equals("81-100: A"), "getGrade(85)");
    }

    // ---------- Урок 1. Массивы и списки ----------
    @Test
    void testFindMax() {
        Lesson1Homework_Arrays_ArraysList1 obj = new Lesson1Homework_Arrays_ArraysList1();
        int actual = obj.findMax(new int[]{3, 7, 2, 9, 1, 5});
        check(actual == 9, "findMax({3,7,2,9,1,5})");
    }

    @Test
    void testReverse() {
        Lesson1Homework_Arrays_ArraysList2 obj = new Lesson1Homework_Arrays_ArraysList2();
        String[] actual = obj.reverse(new String[]{"Яблоко", "Груша", "Слива", "Апельсин"});
        String[] expected = {"Апельсин", "Слива", "Груша", "Яблоко"};
        check(Arrays.equals(expected, actual), "reverse(фрукты)");
    }

    @Test
    void testCalcAverage() {
        // Предполагаем, что в классе Lesson1Homework_Arrays_ArraysList3 есть статический метод
        // calcAverage(List<Integer>) (см. примечание ниже)
        List<Integer> list = List.of(10, 20, 30, 40, 50);
        int actual = Lesson1Homework_Arrays_ArraysList3.calcAverage(list);
        int expected = 30;
        check(expected == actual, "calcAverage(10,20,30,40,50)");
    }

    @Test
    void testRemoveSpecificName() {
        Lesson1Homework_Arrays_ArraysList4 obj = new Lesson1Homework_Arrays_ArraysList4();
        List<String> actual = obj.removeSpecificName(List.of("Анна", "Борис", "Анна", "Виктор"), "Анна");
        List<String> expected = List.of("Борис", "Виктор");
        check(expected.equals(actual), "removeSpecificName(Анна)");
    }
}

