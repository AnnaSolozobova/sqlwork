package ru.netology.web.banklogin.data;

import com.github.javafaker.Faker;
import lombok.Value;

public class DataHelper {
    private static Faker faker = new Faker();

    private DataHelper() {
    }

    @Value
    public static class AutInfo {
        private final String login;
        private final String password;
    }

    public static AutInfo getAuthInfo() {
        return new AutInfo("vasya", "qwerty123");
    }

    public static String getRandomLogin() {
        return faker.name().username();
    }

    public static String getRandomPassword() {
        return faker.internet().password();
    }

    @Value
    public static class VerifyCode {
        private final String verifyCode;
    }

    public static VerifyCode getValidCode(String login) {
        return new VerifyCode(SQLHelper.getVerifyCode(login, "1"));
    }

    public static VerifyCode getRandomCode() {
        return new VerifyCode(String.valueOf(faker.number().numberBetween(100_000, 999_999)));
    }
}
