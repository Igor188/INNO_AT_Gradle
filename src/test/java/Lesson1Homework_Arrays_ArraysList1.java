import org.junit.jupiter.api.Test;

public class Lesson1Homework_Arrays_ArraysList1 {
  @Test
    public void test() {
        int[] numbers = {3, 7, 2, 9, 1, 5};

        // Вызываем метод и выводим результат
        int result = findMax(numbers);
        System.out.println("Самое большое число в массиве: " + result);

    }

    /*1)public int findMax(int[] arr)
    Разработать метод, который находит и возвращает
    самое большое число в переданном массиве
    */
    public int findMax(int[] arr) {
        // Проверка на пустой массив или null
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Массив не может быть пустым или null");
        }

        // Инициализируем максимальное значение первым элементом массива
        int max = arr[0];

        // Проходим по остальным элементам массива
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }

        return max;
    }

}

