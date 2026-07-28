/*
2) public String[] reverse(String[] arr)
Разработать метод, возвращающий новый массив,
в котором элементы исходного массива расположены
в обратном порядке
 */

import org.junit.jupiter.api.Test;

public class Lesson1Homework_Arrays_ArraysList2 {

    @Test
    public void test() {

    }
    public String[] reverse(String[] arr) {
        if (arr == null || arr.length == 0) {
            return arr;
        }

        String[] reversedArr = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reversedArr[i] = arr[arr.length - 1 - i];
        }

        return reversedArr;
    }

    public static void main(String[] args) {
        // ВАЖНО: Сначала создаем объект класса, так как метод нестатический
        Lesson1Homework_Arrays_ArraysList2 reverser = new Lesson1Homework_Arrays_ArraysList2();

        String[] original = {"Яблоко", "Груша", "Слива", "Апельсин"};

        // Вызываем метод через созданный объект
        String[] reversed = reverser.reverse(original);

        // Выводим результаты для проверки
        System.out.println("Исходный массив: " + java.util.Arrays.toString(original));
        System.out.println("Развернутый массив: " + java.util.Arrays.toString(reversed));
    }
}
