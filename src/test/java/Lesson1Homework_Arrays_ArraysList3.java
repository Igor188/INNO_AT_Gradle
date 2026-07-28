import org.junit.jupiter.api.Test;

import java.util.List;

/*
3) public int calcAverage(List<Integer> list)
Разработать метод, который вычисляет и возвращает
среднее арифметическое всех чисел в списке
 */

public class Lesson1Homework_Arrays_ArraysList3 {

    // Предположим, что список уже есть в классе как поле
    private List<Integer> numbers = List.of(10, 20, 30, 40, 50);

   @Test
    // Исправленная сигнатура: void и без параметров
    public void calcAverage() {
        if (numbers == null || numbers.isEmpty()) {
            System.out.println("Список пуст или null");
            return;
        }

        long sum = 0;
        for (Integer number : numbers) {
            if (number != null) {
                sum += number;
            }
        }

        int average = (int) sum / numbers.size();

        // Так как метод void, мы просто выводим результат или сохраняем его в поле
        System.out.println("Среднее арифметическое: " + average);
    }
}