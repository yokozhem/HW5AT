import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class PersonalAccountEditingTest {
    // Создаём экземпляр драйвера для возможности доступа к нему по всему классу
    private static ChromeDriver webDriver;

    // Создаём экземпляр логера
    private static Logger logger = LoggerFactory.getLogger(PersonalAccountEditingTest.class);

    // Создаём список элементов меню для десктопной версии сайта
    private static List<WebElement> menuList;

    // Статический метод запускающийся перед всеми тестами
    @BeforeAll
    static void driverSetting() {

        // Инициализируем драйвер менеджер
        WebDriverManager.chromedriver().setup();

        // Создаём экземпляр класса ChromeOptions
        ChromeOptions chromeOptions = new ChromeOptions();

        /* Передаём в объект chromeOptions настройки для браузера
        --no-sandbox - для работы в докер-контейнере для Chrome
        start-maximized - запуск окна "на весь экран"
        --disable-notification - отключение всплывающих окон
        user-agent=Googlebot/2.1 (+http://www.google.com/bot.html) - запуск в режиме поискового бота
        --incognito - запуск окна в режиме инкогнито
        .setPageLoadTimeout(Duration.ofSeconds(10)) - максимальное время ожидания загрузки страницы
         */
        chromeOptions
                .addArguments("--no-sandbox")
                .addArguments("start-maximized")
                .addArguments("--disable-notification")
                .addArguments("user-agent=Googlebot/2.1 (+http://www.google.com/bot.html)")
                .addArguments("--incognito")
                .setPageLoadTimeout(Duration.ofSeconds(10));

        // Создаём экземпляр класса webdriver
        webDriver = new ChromeDriver(chromeOptions);

        // Implicit Wait (Неявное ожидание) - выставляем 5 секунд (!!! обязательно определяем в коде)
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    // Авторизация на сайте
    @BeforeEach
    void authorization() {

        logger.info("\n[INFO] Предварительная авторизация в личном кабинете перед выполнением теста");
        logger.debug("\n[DEBUG] Тестовые учётные данные: логин tinetoon@mail.ru, пароль te$st" +
                "\n[DEBUG] Проверяем доступность страницы авторизации" +
                "\n[DEBUG] Проверяем title=\"Ъ - Личный кабинет\" страницы после авторизации");

        // Открываем страницу авторизации на сайте kommersant.ru
        Assertions
                .assertDoesNotThrow(()-> webDriver
                                .navigate()
                                .to("https://www.kommersant.ru/lk/login"),
                        "Страница авторизации недоступна");

        // Заполняем поля "Ваш e-mail" и "Введите пароль"
        webDriver.findElement(By.id("email")).sendKeys("tinetoon@mail.ru");
        webDriver.findElement(By.id("password")).sendKeys("te$st");

        // Кликнем на кнопку "Войти"
        webDriver.findElement(By.xpath("//button[@type=\"submit\"]")).click();

        // Ожидание загрузки ссылки "Личный кабинет"
        new WebDriverWait(webDriver, Duration.ofSeconds(10))
                .until(ExpectedConditions
                        .visibilityOfElementLocated(By.xpath("//a[.='Личный кабинет']")));

        // Проверка перехода на страницу "Личный кабинет"
        Assertions.assertEquals("Ъ - Личный кабинет", webDriver.getTitle(),
                "Страница \"Личный кабинет\" недоступна");
    }

    // Тестирование элементов меню
    @Test
    @DisplayName("01 Проверка пунктов меню в ЛК")
    void TestingMenuItems() {

        logger.info("\n[INFO] Проверка пунктов меню в ЛК");
        logger.debug("\n[DEBUG] Порядковые номера элементов в листе пунктов меню должны быть следующие: " +
                "\n[DEBUG] - 0 Уведомления" +
                "\n[DEBUG] - 1 Избранное" +
                "\n[DEBUG] - 2 История чтения" +
                "\n[DEBUG] - 3 Мой профиль" +
                "\n[DEBUG] - 4 Настройки профиля" +
                "\n[DEBUG] - 5 Настройки уведомлений" +
                "\n[DEBUG] - 6 Помощь" +
                "\n[DEBUG] - 7 Сервисы «Ъ»" +
                "\n[DEBUG] - 8 Обратная связь" +
                "\n[DEBUG] - 9 Выход");

        // Инициализируем лист элементов меню строчными элементами с названием пунктов меню
        menuList = webDriver
                .findElements(By
                        .xpath("//div[@class=\"account_menu hide_mobile\"]//span[@class=\"account_menu__name\"]"));

        Assertions
                .assertEquals("Уведомления", menuList.get(0)
                        .getText(), "Пункт меню \"Уведомления\" не найден");
        Assertions
                .assertEquals("Избранное", menuList.get(1)
                        .getText(), "Пункт меню \"Избранное\" не найден");
        Assertions
                .assertEquals("История чтения", menuList.get(2)
                        .getText(), "Пункт меню \"История чтения\" не найден");
        Assertions
                .assertEquals("Мой профиль", menuList.get(3)
                        .getText(), "Пункт меню \"Мой профиль\" не найден");
        Assertions
                .assertEquals("Настройки профиля", menuList.get(4)
                        .getText(), "Пункт меню \"Настройки профиля\" не найден");
        Assertions
                .assertEquals("Настройки уведомлений", menuList.get(5)
                        .getText(), "Пункт меню \"Настройки уведомлений\" не найден");
        Assertions
                .assertEquals("Помощь", menuList.get(6)
                        .getText(), "Пункт меню \"Помощь\" не найден");
        Assertions
                .assertEquals("Сервисы «Ъ»", menuList.get(7)
                        .getText(), "Пункт меню \"Сервисы «Ъ»\" не найден");
        Assertions
                .assertEquals("Обратная связь", menuList.get(8)
                        .getText(), "Пункт меню \"Обратная связь\" не найден");
        Assertions
                .assertEquals("Выход", menuList.get(9)
                        .getText(), "Пункт меню \"Выход\" не найден");
    }

    // Тест редактирования поля "Компания"
    @Test
    @DisplayName("02 Проверка редактирования поля \"Компания\" в ЛК")
    void editingTheCompanyField() {

        logger.info("\n[INFO] Проверка редактирования поля \"Компания\" в ЛК");
        logger.debug("\n[DEBUG] Тестовые учётные данные - Компания: АО \"МЭС\"");

        // Получаем список элементов меню
//        menuList.clear(); - вызывает exception, так как ни данный момент menuList пуст
        menuList = webDriver
                .findElements(By
                        .xpath("//div[@class=\"account_menu hide_mobile\"]//span[@class=\"account_menu__name\"]"));

        // Переходим к настройкам профиля (!!! Исправить прямое обращение по номеру пункта меню)
//        webDriver.findElement(By.xpath("//a[.='Мои настройки']")).click();
        menuList.get(4).click();

        // Ищем поле "Компания"
        WebElement fieldCompany = webDriver.findElement(By.name("Company"));

        // Очищаем поля, заполняем новыми значениями и сохраняем
        fieldCompany.clear();
        fieldCompany.sendKeys("АО \"МЭС\"");
        fieldCompany.submit(); // Используется submit(), так как нет возможности выполнить клик по кнопке "Сохранить настройки"

        // Получаем список элементов меню
        menuList.clear();
        menuList = webDriver
                .findElements(By
                        .xpath("//div[@class=\"account_menu hide_mobile\"]//span[@class=\"account_menu__name\"]"));

        // Возвращаемся на страницу "Личный кабинет" (!!! Исправить прямое обращение по номеру пункта меню)
//        webDriver.findElement(By.xpath("//a[.='Личный кабинет']")).click();
        menuList.get(3).click();

        // Ожидание загрузки ссылки "Личный кабинет"
        new WebDriverWait(webDriver, Duration.ofSeconds(10))
                .until(ExpectedConditions
                        .visibilityOfElementLocated(By.xpath("//a[.='Личный кабинет']")));

        // Проверка данных поля "Компания"
        Assertions.assertEquals("АО \"МЭС\"", webDriver
                .findElement(By.xpath("//p[contains(., 'Компания')]/span")).getText());
    }

    // Тест редактирования поля "Должность"
    @Test
    @DisplayName("03 Проверка редактирования поля \"Должность\" в ЛК")
    void editingThePositionField() {

        logger.info("\n[INFO] Проверка редактирования поля \"Должность\" в ЛК");
        logger.debug("\n[DEBUG] Тестовые учётные данные - Должность: Начальник ПТО");

        // Получаем список элементов меню
        menuList = webDriver
                .findElements(By
                        .xpath("//div[@class=\"account_menu hide_mobile\"]//span[@class=\"account_menu__name\"]"));

        // Переходим к настройкам профиля (!!! Исправить прямое обращение по номеру пункта меню)
        menuList.get(4).click();

        // Ищем поле "Должность"
        WebElement fieldPosition = webDriver.findElement(By.name("Position"));

        // Очищаем поля, заполняем новыми значениями и сохраняем
        fieldPosition.clear();
        fieldPosition.sendKeys("Начальник ПТО");
        fieldPosition.submit();

        // Получаем список элементов меню
        menuList.clear();
        menuList = webDriver
                .findElements(By
                        .xpath("//div[@class=\"account_menu hide_mobile\"]//span[@class=\"account_menu__name\"]"));

        // Возвращаемся на страницу "Личный кабинет" (!!! Исправить прямое обращение по номеру пункта меню)
        menuList.get(3).click();

        // Ожидание загрузки ссылки "Личный кабинет"
        new WebDriverWait(webDriver, Duration.ofSeconds(10))
                .until(ExpectedConditions
                        .visibilityOfElementLocated(By.xpath("//a[.='Личный кабинет']")));

        // Проверка данных поля "Должность"
        Assertions.assertEquals("Начальник ПТО", webDriver
                .findElement(By.xpath("//p[contains(., 'Должность')]/span")).getText());
    }

    // Метод запускающийся после каждого теста
    @AfterEach
    void SignOutOfAccount() {

        // Получаем список элементов меню
        menuList.clear();
        menuList = webDriver
                .findElements(By
                        .xpath("//div[@class=\"account_menu hide_mobile\"]//span[@class=\"account_menu__name\"]"));

        // Возвращаемся на страницу "Личный кабинет"
        menuList.get(9).click();

        // Устанавливаем паузу 2 секунды (!!! необходимо разобраться с ожиданиями)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void exitTheBrowser() {

        // Закрываем браузер
        webDriver.quit();
    }
}
