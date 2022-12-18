package ru.netology.web.banklogin.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginInput = $("[data-test-id='login'] input");
    private SelenideElement loginInputEmptyNotification = $("[data-test-id='login'] [class='input__sub']");
    private SelenideElement passwordInput = $("[data-test-id='password'] input");
    private SelenideElement passwordInputEmptyNotification = $("[data-test-id='password'] [class='input__sub']");
    private SelenideElement loginButton = $("[data-test-id='action-login'].button");
    private SelenideElement errorNotification = $("[data-test-id='error-notification']");

    public LoginPage() {
        loginInput.should(visible);
        passwordInput.should(visible);
        loginButton.should(visible);
        errorNotification.should(hidden);
    }

    public void insert(String login, String password) {
        loginInput.val(login);
        passwordInput.val(password);
        loginButton.click();
    }

    public VerifyPage success() {
        errorNotification.should(hidden);
        return new VerifyPage();
    }

    public void failed() {
        errorNotification.should(visible);
        errorNotification.$("[class='notification__content']").
                should(text("Ошибка! " + "Неверно указан логин или пароль"));
    }

    public void blocked() {
        errorNotification.should(visible);
        errorNotification.$("[class='notification__content']").
                should(text("Ошибка! " + "Пользователь заблокирован"));
    }

    public void emptyLogin() {
        loginInputEmptyNotification.should(text("Поле обязательно для заполнения"));
    }

    public void emptyPassword() {
        passwordInputEmptyNotification.should(text("Поле обязательно для заполнения"));
    }
}
