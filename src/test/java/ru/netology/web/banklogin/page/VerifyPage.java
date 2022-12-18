package ru.netology.web.banklogin.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class VerifyPage {
    private SelenideElement codeInput = $("[data-test-id='code'] input");
    private SelenideElement codeInputEmptyNotification = $("[data-test-id='code'] [class='input__sub']");
    private SelenideElement verifyButton = $("[data-test-id='action-verify'].button");
    private SelenideElement errorNotification = $("[data-test-id='error-notification']");

    public VerifyPage() {
        codeInput.should(visible);
        verifyButton.should(visible);
        errorNotification.should(hidden);
    }

    public void insert(String code) {
        codeInput.val(code);
        verifyButton.click();
    }

    public ru.netology.web.banklogin.page.DashboardPage success() {
        errorNotification.should(hidden);
        return new ru.netology.web.banklogin.page.DashboardPage();
    }

    public void failed() {
        errorNotification.should(visible);
        errorNotification.$("[class='notification__content']").
                should(text("Ошибка! " + "Неверно указан код! Попробуйте ещё раз."));
    }

    public void emptyCode() {
        codeInputEmptyNotification.should(text("Поле обязательно для заполнения"));
    }
}
