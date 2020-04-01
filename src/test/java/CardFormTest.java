import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardFormTest {
    static class Form {
        private WebDriver driver;

        @BeforeAll
        static void setUpAll() {
            System.setProperty("webdriver.chrome.driver", "Browser_plugins/chrome/chromedriver");

            if ((System.getProperty("os.name")).contains("mac")) {
                System.setProperty("webdriver.chrome.driver", "driver/mac/chromedriver");
            } else if ((System.getProperty("os.name")).contains("Linux")) {
                System.setProperty("webdriver.chrome.driver", "driver/Linux/chromedriver");
            } else {
                System.setProperty("webdriver.chrome.driver", "driver/win/chromedriver");
            }
        }

        @BeforeEach
        void setUp() {
            driver = new ChromeDriver();
        }
        @AfterEach
        void tearDown() {
            driver.quit();
            driver = null;
        }
        @Test
        void cardFormLHappyTest() throws InterruptedException {
            //  правильно заполняем форму
            driver.get("http://localhost:9999");
            String title = driver.findElement(By.cssSelector(".heading_theme_alfa-on-white")).getText();
            assertEquals("Заявка на дебетовую карту", title.trim());  //  Заголовок виден и правильно написан
            driver.findElement(By.cssSelector("input.input__control[type=text]")).sendKeys("Сергей");
            driver.findElement(By.cssSelector("input.input__control[type=tel]")).sendKeys("+79991112233");
            driver.findElement(By.cssSelector("form span.checkbox__box")).click();
            driver.findElement(By.className("button__text")).click();
            driver.findElement(By.className("Success_successBlock__2L3Cw")).isDisplayed();
            String successText = driver.findElement(By.className("Success_successBlock__2L3Cw")).getText();
            assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", successText.trim());
        }
        @ParameterizedTest
        @CsvFileSource(resources = "/nameErrorList.csv", numLinesToSkip = 1)
        void cardFormLNameErrorTest(String name, String nameError) throws InterruptedException {
            //  подаем непрриемлемое имя
            driver.get("http://localhost:9999");
            String title = driver.findElement(By.cssSelector(".heading_theme_alfa-on-white")).getText();
            assertEquals("Заявка на дебетовую карту", title.trim());  //  Заголовок виден и правильно написан
            driver.findElement(By.cssSelector("input.input__control[type=text]")).sendKeys(name);
            driver.findElement(By.cssSelector("input.input__control[type=tel]")).sendKeys("+79991112233");
            driver.findElement(By.cssSelector("form span.checkbox__box")).click();
            driver.findElement(By.className("button__text")).click();
            driver.findElement(By.cssSelector("div:nth-child(1) > span > span > span.input__sub")).isDisplayed();
            String errorText = driver.findElement(By.cssSelector("div:nth-child(1) > span > span > span.input__sub")).getText();
            assertEquals(nameError, errorText.trim());
        }
        @ParameterizedTest
        @CsvFileSource(resources = "/phoneErrorList.csv", numLinesToSkip = 1)
        void cardFormPhoneErrorTest(String phoneNumber, String phoneError) throws InterruptedException {
            //  подаем неприемлемый телефонный номер
            driver.get("http://localhost:9999");
            String title = driver.findElement(By.cssSelector(".heading_theme_alfa-on-white")).getText();
            assertEquals("Заявка на дебетовую карту", title.trim());  //  Заголовок виден и правильно написан
            driver.findElement(By.cssSelector("input.input__control[type=text]")).sendKeys("Иванов Иван Иванович");
            driver.findElement(By.cssSelector("input.input__control[type=tel]")).sendKeys(phoneNumber);
            driver.findElement(By.cssSelector("form span.checkbox__box")).click();
            driver.findElement(By.className("button__text")).click();
            driver.findElement(By.cssSelector("div:nth-child(2) > span > span > span.input__sub")).isDisplayed();
            String errorText = driver.findElement(By.cssSelector("div:nth-child(2) > span > span > span.input__sub")).getText();
            assertEquals(phoneError, errorText.trim());
        }
    }
}
