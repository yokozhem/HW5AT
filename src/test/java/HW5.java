
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.Duration;



public class HW5 {

        // Создаём экземпляр драйвера для возможности доступа к нему по всему классу
        private static ChromeDriver webDriver;

        // Создаём экземпляр логера
        private static final Logger logger = (Logger) LoggerFactory.getLogger(HW5.class);

    /*
    Для подготовки и удаления тестовых данных используются методы:
    - методы @BeforeAll (@AfterAll) — запускаются перед (после) всеми тестами один раз.
    - методы @BeforeEach (@AfterEach) — запускаются перед (после) каждым тестом.
    Для @BeforeAll и @AfterAll важно, чтобы они были объявлены как static,
    так как его инициализация происходит до остальных методов.
     */

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

    /*
    .assertDoesNotThrow() - проверка на любую ошибку при загрузке страницы
    webDriver.navigate().to(url: "") == webDriver.get(url: "")
     */

        // Проверка доступности страницы
        @BeforeEach
        void homePageLoaded() {
            Assertions
                    .assertDoesNotThrow(()-> webDriver
                            .navigate()
                            .to("https://www.kommersant.ru/"), "Страница недоступна");
        }

        // Тест авторизации.
        @Test
        @DisplayName("01 Проверка авторизации на сайте")
        void authorization() {

            logger.info("\n[INFO] Проверка авторизации в личном кабинете");
            logger.debug("\n[DEBUG] Тестовые учётные данные: логин tinetoon@mail.ru, пароль te$st" +
                    "\n[DEBUG] Проверяем доступность страницы авторизации" +
                    "\n[DEBUG] Проверяем видимость ссылки \"Личный кабинет\"");

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

            Assertions.assertEquals("Ъ - Личный кабинет", webDriver.getTitle(),
                    "Страница \"Личный кабинет\" недоступна");
        }

        // Метод запускающийся после каждого теста
        @AfterEach
        void exitTheBrowser() {

            // Закрываем браузер
            webDriver.quit();
        }
    }

