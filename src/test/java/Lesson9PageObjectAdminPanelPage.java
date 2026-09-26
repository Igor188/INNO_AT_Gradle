import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.*;
public class Lesson9PageObjectAdminPanelPage {

    // === Элементы страницы ===

    // === Форма аутентификации ===
    // 1. Логин
    SelenideElement inputUsername = $x("//*[@id = 'username']");
    // 2. Пароль
    SelenideElement inputPass = $x("//*[@id = 'password']");
    // 3. кнопка "Войти"
    SelenideElement enterButton = $x("//*[@type='submit']");

    // === Витрина товаров админки ===
    SelenideElement addName = $x("//*[@id = 'n-name']");
    SelenideElement addPrice = $x("//*[@id = 'n-price']");
    SelenideElement createButton = $x("//*[@id = 'add-btn']");
    SelenideElement toastVisible = $x("//*[@id = 'toast-container']");
    SelenideElement exitAdmin = $x("//*[@href= '/']");



    // === Методы взаимодействия ===

     Lesson9PageObjectAdminPanelPage setUsername(String username) {
            inputUsername.sendKeys(username);
            return this;
        }

        Lesson9PageObjectAdminPanelPage setPassword(String password) {
            inputPass.sendKeys(password);
            return this;
        }

        Lesson9PageObjectAdminPanelPage submit() {
            enterButton.click();
            return this;
        }

    Lesson9PageObjectAdminPanelPage createGood(String name, Double price) {
        addName.sendKeys(name);
        addPrice.sendKeys(String.valueOf(price));
        createButton.click();
        return this;
    }

    // Строка таблицы товаров и её элементы

    SelenideElement goodRow(int id) {
        return $x("//tr[.//input[@id='nm-" + id + "']]");
    }

    SelenideElement editNameField(int id) {
        return $("#nm-" + id);
    }

    SelenideElement editPriceField(int id) {
        return $("#pr-" + id);
    }

    SelenideElement updateButton(int id) {
        return goodRow(id).$("[data-action='update']");
    }

    Lesson9PageObjectAdminPanelPage editGood(int id, String editGoodName, Double editGoodPrice) {
        editNameField(id).clear();
        editNameField(id).sendKeys(editGoodName);
        editPriceField(id).clear();
        editPriceField(id).sendKeys(String.valueOf(editGoodPrice));
        updateButton(id).click();
        return this;
    }

    Lesson9PageObjectMainPage goToStorefront() {
        exitAdmin.click();
        return new Lesson9PageObjectMainPage();
    }

    }




















/*
Условие: на основе лекции и вебинара по PageObjects переделать UI-автотесты в проекте на PageObject-архитектуру.

Задание

ЛЕКЦИЯ

Задача 1: написать 2 класса, реализующих PageObject-архитектуру:

PageObject для главной страницы с товарами. Можно добавить на неё любые объекты и коллекции, главное, чтобы их было не меньше 5.
Дополнительно необходимо написать методы взаимодействия с каждым из них в зависимости от типа (click(), sendKeys());
PageObject для формы авторизации в админке. Здесь должна быть полная функциональность (3 элемента: логин, пароль, кнопка «Войти»).

Для всех описанных элементов должны быть базовые проверки (элемент видно, текстовое поле содержит текст),
организованные в PageAssert-классе или внутри самого Page.

Критерии проверки

Задача 1:

1.1* (обязательная) Проект содержит PageObject-класс для основной страницы с товарами — 3 б.

1.2* (обязательная) Проект содержит PageObject-класс для страницы входа в админку — 3 б.

1.3 Все элементы, входящие в PageObject-классы, содержат методы для взаимодействия с элементами — 3 б.

1.4 Для всех элементов есть базовые проверки — 3 б.

*/


/*
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class Lesson9PageObjectMainPage {
    SelenideElement cartButton = $x("//*[@id='cart-button']");
}
*/
