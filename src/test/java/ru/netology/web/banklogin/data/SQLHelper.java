package ru.netology.web.banklogin.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLHelper {
    private static QueryRunner runner;
    private static Connection conn;

    @SneakyThrows
    public static void setUp() {
        runner = new QueryRunner();
        conn = DriverManager.getConnection
                ("jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    @SneakyThrows
    public static void setDown() {
        setUp();
        reloadVerifyCodeTable();
        var sqlQueryDeleteTransaction = "DELETE FROM card_transactions;";
        var sqlQueryDeleteCards = "DELETE FROM cards;";
        var sqlQueryDeleteUsers = "DELETE FROM users;";
        runner.update(conn, sqlQueryDeleteTransaction);
        runner.update(conn, sqlQueryDeleteCards);
        runner.update(conn, sqlQueryDeleteUsers);
    }

    @SneakyThrows
    public static void reloadVerifyCodeTable() {
        setUp();
        var sqlQueryDeleteCodes = "DELETE FROM auth_codes;";
        runner.update(conn, sqlQueryDeleteCodes);
    }

    @SneakyThrows
    public static String getVerifyCode(String login, String sqlLimit) {
        setUp();
        var sqlQueryCode = "SELECT code FROM auth_codes " +
                "JOIN users ON user_id = users.id " +
                "WHERE login IN (?) " +
                "ORDER BY created DESC LIMIT " + sqlLimit + ";";
        return runner.query(conn, sqlQueryCode, new ScalarHandler<String>(), login);
    }

    @SneakyThrows
    public static String getUserStatus(String login) {
        setUp();
        var sqlQueryUsers = "SELECT status FROM users WHERE login IN (?);";
        return runner.query(conn, sqlQueryUsers, new ScalarHandler<String>(), login);
    }

    @SneakyThrows
    public static void setUserStatus(String login, String userStatus) {
        setUp();
        var sqlQueryuserStatus = "UPDATE users SET status = '" + userStatus + "' WHERE login IN(?);";
        runner.update(conn, sqlQueryuserStatus, login);
    }
}
