package ru.netology.web.banklogin.test;

import org.junit.jupiter.api.*;
import ru.netology.web.banklogin.data.DataHelper;
import ru.netology.web.banklogin.data.SQLHelper;
import ru.netology.web.banklogin.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class BankLoginTests {

    DataHelper.AutInfo user;
    DataHelper.VerifyCode code;
    LoginPage loginPage;

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
        user = DataHelper.getAuthInfo();
        loginPage = new LoginPage();
    }

    @AfterEach
    public void setDown() {
        SQLHelper.reloadVerifyCodeTable();
        SQLHelper.setUserStatus(user.getLogin(), "active");
    }

    @AfterAll
    static void setDownClass() {
        SQLHelper.setDown();
    }

    @DisplayName("Успешная авторизация")
    @Test
    public void loginVerification() {
        loginPage.insert(user.getLogin(), user.getPassword());
        var verifyPage = loginPage.success();
        code = DataHelper.getValidCode(user.getLogin());
        verifyPage.insert(code.getVerifyCode());
        var dashboardPage = verifyPage.success();
    }

    @DisplayName("Отказв авторизации с устаревшим кодом потверждения")
    @Test
    public void shouldNoAuthWithOldestVerifyCode() {
        loginPage.insert(user.getLogin(), user.getPassword());
        var verifyPage = loginPage.success();
        code = DataHelper.getValidCode(user.getLogin());
        verifyPage.insert(code.getVerifyCode());
        var dashboardPage = verifyPage.success();

        open("http://localhost:9999");
        loginPage.insert(user.getLogin(), user.getPassword());
        verifyPage = loginPage.success();
        verifyPage.insert(code.getVerifyCode());
        verifyPage.failed();
    }

    @DisplayName("Отказ в авторизации с рандомным кодом подтверждения")
    @Test
    public void shouldNoAuthWithInvalidVerifyCode() {
        loginPage.insert(user.getLogin(), user.getPassword());
        var verifyPage = loginPage.success();
        code = DataHelper.getRandomCode();
        verifyPage.insert(code.getVerifyCode());
        verifyPage.failed();
    }

    @DisplayName("Отказ в авторизации с невалидным логином")
    @Test
    public void shouldNoAuthWithInvalidLogin() {
        var login = DataHelper.getRandomLogin();
        loginPage.insert(login, user.getPassword());
        loginPage.failed();
    }

    @DisplayName("Отказ в авторизации с невалидным паролем")
    @Test
    public void shouldNoAuthWithInvalidPassword() {
        var password = DataHelper.getRandomPassword();
        loginPage.insert(user.getLogin(), password);
        loginPage.failed();
    }

    @DisplayName("Блокировка пользователя после трех попыток ввода неправильного пароля")
    @Test
    public void ShouldBlockUserAfterThreeInputInvalidPassword() {
        var password = DataHelper.getRandomPassword();
        loginPage.insert(user.getLogin(), password);
        loginPage.failed();

        open("http://localhost:9999");
        password = DataHelper.getRandomPassword();
        loginPage.insert(user.getLogin(), password);
        loginPage.failed();

        open("http://localhost:9999");
        password = DataHelper.getRandomPassword();
        loginPage.insert(user.getLogin(), password);
        loginPage.failed();

        assertEquals("blocked", SQLHelper.getUserStatus(user.getLogin()));

        open("http://localhoct:9999");
        loginPage.insert(user.getLogin(), user.getPassword());
        loginPage.blocked();
    }

    @DisplayName("Сообщение о пустом поле Логин")
    @Test
    public void shouldNotificationWithEmptyLogin() {
        loginPage.insert(null, user.getPassword());
        loginPage.emptyLogin();
    }

    @DisplayName("Сообщение о пустом поле Пароль")
    @Test
    public void ShouldNotificationWithEmptyPassword() {
        loginPage.insert(user.getLogin(), null);
        loginPage.emptyPassword();
    }

    @DisplayName("Сообщение о пустом поле проверочног кода")
    @Test
    public void shouldNotificationWithEmptyVerifyCode() {
        loginPage.insert(user.getLogin(), user.getPassword());
        var verifyPage = loginPage.success();
        verifyPage.insert(null);
        verifyPage.emptyCode();
    }
}
