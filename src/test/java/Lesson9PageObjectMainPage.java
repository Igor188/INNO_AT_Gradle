import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.*;


public class Lesson9PageObjectMainPage {

    // === Элементы страницы ===

    // 1. Ссылка перехода в админку
    SelenideElement adminLink = $("[href='/admin']");

    // 2. Кнопка открытия корзины
    SelenideElement openCart = $x("//*[@id = 'open-cart-btn']");

    // 3. Кнопка "Оформить заказ" (в корзине)
    SelenideElement makeOrderButton = $("#makeOrder");

    // 4. Контейнер тостов
    SelenideElement toastContainer = $x("//*[@id = 'toast-container']");

    // 5. Итоговая сумма в корзине
    SelenideElement totalPrice = $x("//*[@id='total-price']");

    // 6. Карточка товара по имени на витрине
    SelenideElement productCard(String name) {
        return $x("//div[contains(@class,'product-card')][@data-name='" + name + "']");
    }

    // 7. Кнопка "В корзину" в карточке товара
    SelenideElement addToCartButton(String name) {
        return $x("//*[@data-action='add-to-cart'][@data-name='" + name + "']");
    }

    // 8. Карточка товара в корзине
    SelenideElement cartItem(String name) {
        return $$(".cart-item").findBy(Condition.text(name));
    }

    // 9. Количество товара в корзине
    SelenideElement qty(String name) {
        return cartItem(name).$(".qty-controls span");
    }

    // 10. Сумма позиции в корзине
    SelenideElement itemTotal(String name) {
        return cartItem(name).$("div:nth-child(3)");
    }


    // === Методы взаимодействия ===

    Lesson9PageObjectMainPage open(String url) {
        Selenide.open(url);
        return this;
    }

    Lesson9PageObjectMainPage refreshPage() {
        Selenide.refresh();
        return this;
    }

    Lesson9PageObjectAdminPanelPage goToAdmin() {
        adminLink.click();
        return new Lesson9PageObjectAdminPanelPage();
    }

    Lesson9PageObjectMainPage clickAddToCart(String name) {
        addToCartButton(name).click();
        return this;
    }

    Lesson9PageObjectMainPage openCart() {
        openCart.click();
        return this;
    }

    Lesson9PageObjectMainPage clickMakeOrder() {
        makeOrderButton.click();
        return this;
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
