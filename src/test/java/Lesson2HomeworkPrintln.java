import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Lesson2HomeworkPrintln {

    Random random = new Random();

    @BeforeEach
    void printStart() {
        System.out.println("Test method start\n" +
                "========================");
    }

    @AfterEach
    void printEnd() {
        System.out.println("Test method end\n" +
                "========================");
        }

    // 1. isEven – один раз, случайное число от 1 до 100
    @Test
       void testIsEvenOnce() {
           int number = random.nextInt(1, 101);  // 1..100 включительно
           System.out.println("Calling isEven(" + number + ")");
           boolean result = Lesson1Homework_Logic_ternarii.isEven(number);
           System.out.println("Result: " + result);
       }


    // 2. checkAccess – 20 раз, возраст 0–99
    @RepeatedTest(20)
    void testCheckAccessRepeated() {
        int age = random.nextInt(100);
        System.out.println("Calling checkAccess(" + age + ")");
        String access = Lesson1Homework_Logic_ternarii.checkAccess(age);
        System.out.println("Result: " + access);
    }


    // 3. getGrade – параметризованный тест с 10 случайными оценками 0–100
    @ParameterizedTest
    @MethodSource("randomScores")
    void testGetGradeParameterized(int score) {
        System.out.println("Calling getGrade(" + score + ")");
        String grade = Lesson1Homework_Logic_ternarii.getGrade(score);
        System.out.println("Result: " + grade);
    }

    // Статический метод-источник для параметризованного теста
    static Stream<Arguments> randomScores() {
        Random r = new Random();
        return IntStream.range(0, 10)    // 10 случайных чисел
                .map(i -> r.nextInt(101)) // от 0 до 100
                .boxed()
                .map(Arguments::of);
    }

}

