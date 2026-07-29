import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class Lesson3Homework_Assert_Task1 {
    private final Random random = new Random();

    // 1. Тест на boolean метод – проходит (isEven(4) = true)
    @Test
    void testIsEvenPass() {
        boolean actual_result = Lesson1Homework_Logic_ternarii.isEven(4);
        assertThat(actual_result)
                .as("Проверка, что 4 – чётное число")
                .isTrue();
    }

    // 2. Тест на boolean метод – специально падает с пояснением
    @Test
    void testIsPositiveFail() {
        int n = random.nextInt(-10, 0);
        boolean actual_result = Lesson1Homework_Logic_ternarii.isPositive(n);
        assertThat(actual_result)
                .as("Проверка на положительные значения для числа %d:", n)
                .isTrue();
    }


    // 3. Тест на список – успешно проходит
    @Test
    void testRemoveSpecificNamePass() {
        List<String> input = List.of("Анна", "Борис", "Анна", "Виктор");
        List<String> actual_result = new Lesson1Homework_Arrays_ArraysList4()
                .removeSpecificName(input, "Анна");

        assertThat(actual_result)
                .as("После удаления 'Анна' должен остаться список [Борис, Виктор]")
                .isEqualTo(List.of("Борис", "Виктор"));
    }

    // 4. Тест на метод, возвращающий список – специально падает
    @Test
    void testRemoveSpecificNameFail() {
        List<String> input = List.of("Анна", "Борис", "Анна", "Виктор");
        List<String> actual = new Lesson1Homework_Arrays_ArraysList4()
                .removeSpecificName(input, "Анна");
        // Намеренно ожидаем неправильный список, чтобы ассерт упал
        List<String> expected = List.of("Анна", "Борис", "Виктор"); // ошибка: Анна не удалена
        assertEquals(expected, actual,
                "Ожидалось, что 'Анна' останется в списке, но метод её удалил");
    }

}
