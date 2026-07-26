import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class Lesson1Homework {
   @Test

    public void logic_ternarii() {
        System.out.println(isEven(5));
        System.out.println(isPositive(-2));
        System.out.println(checkAccess(17));
        System.out.println(getGrade(-5));
    }


    //1) public boolean isEven(int n) Разработать метод, который возвращает true, если число чётное
    public static boolean isEven(int n) {
        return (n % 2 == 0) ? true : false;
    }

    /*2) public boolean isPositive(int n) Разработать метод, который проверяет знак
    через тернарный оператор: возвращает true, если число
    больше или равно 0, и false, если меньше*/
    public static boolean isPositive(int n) {
        return (n >= 0) ? true : false;

    }


    /*3) public String checkAccess(int age)
    Разработать метод, который возвращает Allowed,
    если age больше 18, иначе — Denied*/
    public static String checkAccess(int age) {
        return (age > 18) ? "Allowed" : "Denied";

    }

    /*4) public String getGrade(int score)
    Разработать метод, который преобразует баллы
    (0–100) в символ оценки:
    + 0–20: E
    + 21–40: D
    + 41–60: C
    + 61–80: B
    + 81–100:A */

    public static String getGrade(int score) {
        if (score >= 0 && score <= 20) {
            return "0-20: E";
        } else if (score >= 21 && score <= 40) {
            return "21-40: D";
        } else if (score >= 41 && score <= 60) {
            return "41-60: C";
        } else if (score >= 61 && score <= 80) {
            return "61-80: B";
        } else if (score >= 81 && score <= 100) {
            return "81-100: A";
        } else {
            // Обработка случаев, когда балл вне диапазона 0-100
            return "Invalid score";
        }
    }
}



