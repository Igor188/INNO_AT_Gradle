import org.junit.jupiter.api.Test;

import java.util.List;

/*
3) public int calcAverage(List<Integer> list)
Разработать метод, который вычисляет и возвращает
среднее арифметическое всех чисел в списке
 */

public class Lesson1Homework_Arrays_ArraysList3 {

    // Метод для тестового вызова (возвращает int)
    public static int calcAverage(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        long sum = 0;
        for (Integer number : list) {
            if (number != null) {
                sum += number;
            }
        }
        return (int) sum / list.size();
    }


    @Test
    public void calcAverageOld() {
        List<Integer> numbers = List.of(10, 20, 30, 40, 50);
        int average = calcAverage(numbers);
        System.out.println("Среднее арифметическое: " + average);
    }
}