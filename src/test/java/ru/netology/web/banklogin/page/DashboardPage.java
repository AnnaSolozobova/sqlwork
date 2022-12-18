package ru.netology.web.banklogin.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    private SelenideElement dashboardHeader = $("[data-test-id='dashboard']");

    public DashboardPage() {
        dashboardHeader.should(visible);
    }
}