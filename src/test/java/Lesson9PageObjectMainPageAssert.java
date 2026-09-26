import org.assertj.core.api.AbstractAssert;
import static com.codeborne.selenide.Condition.*;

public class Lesson9PageObjectMainPageAssert extends AbstractAssert<Lesson9PageObjectMainPageAssert, Lesson9PageObjectMainPage> {
    public Lesson9PageObjectMainPageAssert(Lesson9PageObjectMainPage actual) {
        super(actual, Lesson9PageObjectMainPageAssert.class);
    }

    public Lesson9PageObjectMainPageAssert adminLinkIsVisible() {
        isNotNull();
        actual.adminLink.should(visible);
        return this;
    }

    public Lesson9PageObjectMainPageAssert cartButtonIsVisible() {
        isNotNull();
        actual.openCart.should(visible);
        return this;
    }

    public Lesson9PageObjectMainPageAssert makeOrderButtonIsVisible() {
        isNotNull();
        actual.makeOrderButton.should(visible);
        return this;
    }
    public Lesson9PageObjectMainPageAssert productCardIsVisible(String name) {
        isNotNull();
        actual.productCard(name).should(visible);
        return this;
    }

    public Lesson9PageObjectMainPageAssert productNameEquals(String name) {
        isNotNull();
        actual.productCard(name).$("h4").should(text(name));
        return this;
    }

    public Lesson9PageObjectMainPageAssert productPriceContains(String name, Double price) {
        isNotNull();
        actual.productCard(name).$("div:not([class])")
                .should(partialText(String.valueOf(price)));
        return this;
    }

    public Lesson9PageObjectMainPageAssert toastHasText(String expected) {
        isNotNull();
        actual.toastContainer.should(text(expected));
        return this;
    }

    public Lesson9PageObjectMainPageAssert cartItemVisible(String name) {
        isNotNull();
        actual.cartItem(name).should(visible);
        return this;
    }

   public Lesson9PageObjectMainPageAssert qtyEquals(String name, int qty) {
        isNotNull();
        actual.qty(name).should(text(String.valueOf(qty)));
        return this;
    }

    public Lesson9PageObjectMainPageAssert itemTotalContains(String name, double expected) {
        isNotNull();
        actual.itemTotal(name).should(partialText(String.valueOf(expected)));
        return this;
    }

    public Lesson9PageObjectMainPageAssert totalPriceContains(String expected) {
        isNotNull();
        actual.totalPrice.should(partialText(expected));
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
import org.assertj.core.api.AbstractAssert;

import static com.codeborne.selenide.Condition.visible;

public class Lesson9PageObjectMainPageAssert extends AbstractAssert<Lesson9PageObjectMainPageAssert, Lesson9PageObjectMainPage> {
public Lesson9PageObjectMainPageAssert(Lesson9PageObjectMainPage actual) {
    super(actual, Lesson9PageObjectMainPageAssert.class);
    }

    public Lesson9PageObjectMainPageAssert cartButtonIsVisible() {
    actual.cartButton.should(visible);

    return this;
    }
}

*/