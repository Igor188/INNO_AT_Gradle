import org.junit.jupiter.api.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Lesson5HomeworkSelectorsLocators {

    @BeforeEach
    void printStart() {
        System.out.println("========================\n" +
                "Test method start\n");
    }

    @AfterEach
    void printEnd() {
        System.out.println("\nTest method end\n" +
                "========================\n");
    }

    //1.1. Список названий товаров. CSS-локатор
    @Test
    @Tag("UI")
    @Order(1)
    void printListGoodsNamesLocators() {
        String css = ".product-card h4";
        System.out.println("CSS-локатор 'Список названий товаров' : " + css);
    }

    //1.2. Список цен товаров.  CSS-локатор
    @Test
    @Tag("UI")
    @Order(2)
    void printListPricesLocators() {
        String css = ".product-card [style*=\"font-weight:bold\"]";
        System.out.println("CSS-локатор 'Список цен товаров' : " + css);
    }


    //1.3. Название товара с ценой 25 (добавьте через админку).  XPath-локатор
    @Test
    @Tag("UI")
    @Order(3)
    void printProductNameWithPrice25Locators() {
        String xpath = "//tr[.//input[@value='25']]//input[@type='text']/@value";
        System.out.println("XPath-локатор 'Название товара с ценой 25' : " + xpath);
    }



    //1.4. Цена товара с названием «Стакан» (добавьте через админку). XPath-локатор
    @Test
    @Tag("UI")
    @Order(4)
    void printPriceOfProductGlassLocators() {
        String xpath = "//tr[.//input[@value='Стакан']]//input[@type='number']/@value";
        System.out.println("XPath-локатор 'Цена товара с названием «Стакан»' : " + xpath);
    }

    //1.5. Список карточек товаров в корзине. CSS-локатор
    @Test
    @Tag("UI")
    @Order(5)
    void printCartItemsLocators() {
        String css = "#cart-items > .cart-item";
        System.out.println("CSS-локатор 'Список карточек товаров в корзине' : " + css);
    }



    //1.6. Кнопка корзины. XPath-локатор/CSS-локатор
    @Test
    @Tag("UI")
    @Order(6)
    void printCartButtonLocators() {
        String xpath = "//*[@id='open-cart-btn']";
        System.out.println("XPath-локатор 'Кнопка корзины' : " + xpath);
        String css = "[id='open-cart-btn']";
        System.out.println("CSS-локатор 'Кнопка корзины' : " + css);

    }

}




/*
Lessson5LessonWork

CSS-selector
[href='/admin'] - кнопка администрирование
[data-action='add-to-cart'] - кнопка в корзину у списка товаров
#password - поле ввода пароля

XPath-локатор
//*
//*[@id='open-cart-btn'] - корзина
//*[@type='password'] - поле ввода пароля
//*[@type='submit'] - поле "sign in"

+

XPath-оси

//*[@class='qty-controls']//child::*[@data-step='-1'] - кнопка "-" на вкладке товары
//*[@class='qty-controls']//child::*[@data-step='1'] - кнопка "+" на вкладке товары
//*[@class='qty-controls']//child::*[@data-step='-1']/following-sibling::input - кнопка "количество" на вкладке товары
//*[@id = 'tbody']//descendant::*[contains(@class, 'upd')] - кнопка сохранить в админке
//*[@id = 'toast-container'] - тост по обновлению
 */


