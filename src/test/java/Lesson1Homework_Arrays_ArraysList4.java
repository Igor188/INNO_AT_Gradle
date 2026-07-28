import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/*
4) public List<String>
removeSpecificName(List<String> list, String
nameToRemove)
Разработать метод, принимающий список и имя,
которое нужно исключить. Возвращает новый список,
не содержащий указанного имени
 */

public class Lesson1Homework_Arrays_ArraysList4 {

    public List<String> removeSpecificName(List<String> list, String nameToRemove) {
        // Создаем новый список для хранения результата
        List<String> result = new ArrayList<>();

        // Проверяем, не null ли исходный список, чтобы избежать NullPointerException
        if (list == null) {
            return result; // Возвращаем пустой список, если входной был null
        }

        // Проходим по каждому элементу исходного списка
        for (String item : list) {
            // Добавляем элемент в новый список только если он НЕ равен удаляемому имени
            // Используем .equals() для сравнения строк, а также проверяем на null
            if (item != null && !item.equals(nameToRemove)) {
                result.add(item);
            }
        }

        return result;
    }

    // Пример использования для проверки
    @Test
    public void main() {
        Lesson1Homework_Arrays_ArraysList4 utils = new Lesson1Homework_Arrays_ArraysList4();

        List<String> names = List.of("Анна", "Борис", "Анна", "Виктор", "Дмитрий");
        String toRemove = "Анна";

        List<String> filteredNames = utils.removeSpecificName(names, toRemove);

        System.out.println("Исходный список: " + names);
        System.out.println("Удаляем: " + toRemove);
        System.out.println("Результат: " + filteredNames);
        // Ожидаемый вывод: [Борис, Виктор, Дмитрий]
    }
}
