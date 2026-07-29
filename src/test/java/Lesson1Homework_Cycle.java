import org.junit.jupiter.api.Test;

public class Lesson1Homework_Cycle {
    @Test

    public void Cycle_print() {

        System.out.println(blastOff(5));

        System.out.println(sumToN(5));

        System.out.println(getEvenInRange(1,10));

    }



    /*1) public String blastOff(int start)
    Разработать метод, который принимает стартовое
    число (например, 5) и возвращает строку со всеми
    числами до 1 и словом «Поехали!» в конце
    (например, «5 4 3 2 1 Поехали!»)
    */
    public String blastOff(int start) {
        StringBuilder result = new StringBuilder(); //Конструктор - создаем объект чтобы записать туда числа по порядку

        // Проходим от стартового числа до 1
        for (int i = start; i >= 1; i--) {
            result.append(i).append(" ");
        }

        // Добавляем финальное слово
        result.append("Поехали!");

        return result.toString();
    }


    /*2) public boolean hasBug(String[] messages)
    Разработать метод, который принимает массив
    строк и возвращает true, если хотя бы одна строка
    равна Bug
    */
    public boolean hasBug(String[] messages){
// Проходим по каждому элементу массива
        for (String message : messages) {
            // Проверяем, равна ли текущая строка "Bug"
            if ("Bug".equals(message)) {
                return true; // Если нашли, сразу возвращаем true
            }
        }
        // Если цикл завершился, значит "Bug" не был найден
        return false;
    }


    /*3)public int sumToN(int n)
    Разработать метод, который возвращает сумму
    всех целых чисел от 1 до n
    */
    public int sumToN(int n){
        int sum = 0;
        // Проходим от 1 до n включительно
        for (int i = 1; i <= n; i++) {
            sum += i; // Добавляем текущее число к сумме
        }
        return sum;
    }

/*
4)public String getEvenInRange(int start, int end)
Разработать метод, который принимает границы
диапазона и возвращает строку, состоящую только
из чётных чисел внутри этого промежутка
 */

    public String getEvenInRange(int start, int end){
        StringBuilder result = new StringBuilder();//Конструктор - создаем объект чтобы записать туда числа по порядку
        for (int i = start; i<= end; i++){
            if(i%2 == 0){
                result.append(i).append(" ");
            }
        }
        return result.toString();
    }

}



