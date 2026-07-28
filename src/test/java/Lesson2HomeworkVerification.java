import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;


public class Lesson2HomeworkVerification {

    private final Lesson1Homework_Logic_ternarii homework = new Lesson1Homework_Logic_ternarii();
    private final Random random = new Random();

    // ================= isEven (3 метода) =================
    @Test
    void isEvenTest() {
        int n = random.nextInt(1, 101);
        boolean expected = n % 2 == 0;
        boolean actual = Lesson1Homework_Logic_ternarii.isEven(n);
        printResult(expected == actual, "isEven(" + n + ")");
    }

    @RepeatedTest(5)
    void isEvenRepeated() {
        int n = random.nextInt(1, 101);
        boolean expected = n % 2 == 0;
        boolean actual = Lesson1Homework_Logic_ternarii.isEven(n);
        printResult(expected == actual, "isEven(" + n + ")");
    }

    @ParameterizedTest
    @MethodSource("isEvenData")
    void isEvenParameterized(int n, boolean expected) {
        boolean actual = Lesson1Homework_Logic_ternarii.isEven(n);
        printResult(expected == actual, "isEven(" + n + ")");
    }

    static Stream<Arguments> isEvenData() {
        return Stream.of(
                Arguments.of(2, true),
                Arguments.of(3, false),
                Arguments.of(100, true),
                Arguments.of(99, false),
                Arguments.of(0, true)
        );
    }

    // ================= isPositive (3 метода) =================
    @Test
    void isPositiveTest() {
        int n = random.nextInt(-100, 101);
        boolean expected = n >= 0;
        boolean actual = Lesson1Homework_Logic_ternarii.isPositive(n);
        printResult(expected == actual, "isPositive(" + n + ")");
    }

    @RepeatedTest(5)
    void isPositiveRepeated() {
        int n = random.nextInt(-100, 101);
        boolean expected = n >= 0;
        boolean actual = Lesson1Homework_Logic_ternarii.isPositive(n);
        printResult(expected == actual, "isPositive(" + n + ")");
    }

    @ParameterizedTest
    @MethodSource("isPositiveData")
    void isPositiveParameterized(int n, boolean expected) {
        boolean actual = Lesson1Homework_Logic_ternarii.isPositive(n);
        printResult(expected == actual, "isPositive(" + n + ")");
    }

    static Stream<Arguments> isPositiveData() {
        return Stream.of(
                Arguments.of(-5, false),
                Arguments.of(0, true),
                Arguments.of(10, true),
                Arguments.of(-1, false)
        );
    }

    // ================= checkAccess (3 метода) =================
    @Test
    void checkAccessTest() {
        int age = random.nextInt(100);
        String expected = age > 18 ? "Allowed" : "Denied";
        String actual = Lesson1Homework_Logic_ternarii.checkAccess(age);
        printResult(expected.equals(actual), "checkAccess(" + age + ")");
    }

    @RepeatedTest(5)
    void checkAccessRepeated() {
        int age = random.nextInt(100);
        String expected = age > 18 ? "Allowed" : "Denied";
        String actual = Lesson1Homework_Logic_ternarii.checkAccess(age);
        printResult(expected.equals(actual), "checkAccess(" + age + ")");
    }

    @ParameterizedTest
    @MethodSource("checkAccessData")
    void checkAccessParameterized(int age, String expected) {
        String actual = Lesson1Homework_Logic_ternarii.checkAccess(age);
        printResult(expected.equals(actual), "checkAccess(" + age + ")");
    }

    static Stream<Arguments> checkAccessData() {
        return Stream.of(
                Arguments.of(17, "Denied"),
                Arguments.of(18, "Denied"),   // строго больше 18
                Arguments.of(19, "Allowed"),
                Arguments.of(0, "Denied"),
                Arguments.of(99, "Allowed")
        );
    }

    // ================= getGrade (3 метода) =================
    @Test
    void getGradeTest() {
        int score = random.nextInt(-10, 111);  // включая выход за границы
        String expected = expectedGrade(score);
        String actual = Lesson1Homework_Logic_ternarii.getGrade(score);
        printResult(expected.equals(actual), "getGrade(" + score + ")");
    }

    @RepeatedTest(5)
    void getGradeRepeated() {
        int score = random.nextInt(-10, 111);
        String expected = expectedGrade(score);
        String actual = Lesson1Homework_Logic_ternarii.getGrade(score);
        printResult(expected.equals(actual), "getGrade(" + score + ")");
    }

    @ParameterizedTest
    @CsvSource({
            "95, 81-100: A",
            "70, 61-80: B",
            "50, 41-60: C",
            "30, 21-40: D",
            "10, 0-20: E",
            "101, Invalid score",
            "-5, Invalid score"
    })
    void getGradeParameterizedCSV(int score, String expected) {
        String actual = Lesson1Homework_Logic_ternarii.getGrade(score);
        printResult(expected.equals(actual), "getGrade(" + score + ")");
    }

    // Вспомогательный метод для расчёта ожидаемой оценки
    private String expectedGrade(int score) {
        if (score >= 0 && score <= 20) return "0-20: E";
        else if (score >= 21 && score <= 40) return "21-40: D";
        else if (score >= 41 && score <= 60) return "41-60: C";
        else if (score >= 61 && score <= 80) return "61-80: B";
        else if (score >= 81 && score <= 100) return "81-100: A";
        else return "Invalid score";
    }

    // Вывод результата проверки без assert
    private void printResult(boolean passed, String testInfo) {
        if (passed) {
            System.out.println("TEST PASSED: " + testInfo);
        } else {
            System.out.println("TEST FAILED: " + testInfo);
        }
    }
}