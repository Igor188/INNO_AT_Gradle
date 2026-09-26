import com.codeborne.selenide.Condition;
import org.assertj.core.api.AbstractAssert;

import static com.codeborne.selenide.Condition.*;

public class Lesson9PageObjectAdminPanelPageAssert extends AbstractAssert<Lesson9PageObjectAdminPanelPageAssert, Lesson9PageObjectAdminPanelPage> {
public Lesson9PageObjectAdminPanelPageAssert(Lesson9PageObjectAdminPanelPage actual) {
    super(actual, Lesson9PageObjectAdminPanelPageAssert.class);
    }

    public Lesson9PageObjectAdminPanelPageAssert loginFieldVisible() {
        isNotNull();
        actual.inputUsername.should(visible);
        return this;
    }

    public Lesson9PageObjectAdminPanelPageAssert passwordFieldVisible() {
        isNotNull();
        actual.inputPass.should(visible);
        return this;
    }

    public Lesson9PageObjectAdminPanelPageAssert createButtonVisible() {
        isNotNull();
        actual.enterButton.should(visible);
        return this;
    }

    public Lesson9PageObjectAdminPanelPageAssert toastHasText(String expected) {
        isNotNull();
        actual.toastVisible.should(Condition.text(expected));
        return this;
    }

    public Lesson9PageObjectAdminPanelPageAssert goodRowVisible(int id) {
        isNotNull();
        actual.goodRow(id).shouldBe(visible);
        return this;
    }

    public Lesson9PageObjectAdminPanelPageAssert goodNameEquals(int id, String expected) {
        isNotNull();
        actual.editNameField(id).should(value(expected));
        return this;
    }

    public Lesson9PageObjectAdminPanelPageAssert goodPriceEquals(int id, String expected) {
        isNotNull();
        actual.editPriceField(id).should(value(expected));
        return this;
    }

    public Lesson9PageObjectAdminPanelPageAssert usernameHasText(String expected) {
        isNotNull();
        actual.inputUsername.shouldHave(value(expected));
        return this;
    }

    public Lesson9PageObjectAdminPanelPageAssert passwordHasText(String expected) {
        isNotNull();
        actual.inputPass.shouldHave(value(expected));
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
