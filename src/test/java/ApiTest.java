import api.Auth;
import api.Cards;
import api.Login;
import api.Transfer;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import util.Dao;

import java.sql.SQLException;


import static org.junit.jupiter.api.Assertions.*;

public class ApiTest {
    Login login = new Login();
    Auth auth = new Auth();
    Cards cards = new Cards();
    Transfer transfer = new Transfer();

    @AfterAll
    static void clearAll() throws SQLException {
        Dao.clearAllTables();

    }

    @Test
    void shouldReturn200forValidLoginInfo() {
        Response httpResponse = login.sendPostRequest("vasya", "qwerty123");
        assertEquals(httpResponse.statusCode(), 200);
    }

    @Test
    void shouldReturn400forInvalidLoginInfo() {
        Response httpResponse = login.sendPostRequest("kolya", "password");
        int expectedStatusCode = 400;
        String expectedJsonValue = "AUTH_INVALID";
        int actualStatusCode = httpResponse.getStatusCode();
        String actualJsonValue = httpResponse.jsonPath().getString("code");
        assertEquals(expectedJsonValue, actualJsonValue);
        assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    void shouldSubmitValidAuthCode() throws SQLException {
        Dao.clearAuthCodes();
        login.sendPostRequest("vasya", "qwerty123");
        Response response = auth.sendAuthCode("vasya", Dao.getAuthCode(Dao.getId("vasya")));
        int expectedStatusCode = 200;
        int actualStatusCode = response.getStatusCode();
        assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    void shouldReturnCardsJson() throws SQLException {
        Dao.clearAuthCodes();
        Dao.setBalanceTo10000();
        login.sendPostRequest("vasya", "qwerty123");
        auth.sendAuthCode("vasya", Dao.getAuthCode(Dao.getId("vasya")));
        String token = auth.getToken("vasya");
        String[] cardsIdNumbers = cards.getCardsNumbers(token);
        int[] cardsStrBalance = cards.getCardsBalance(token);
        String expectedCardOneNumber = "**** **** **** 0002";
        String expectedCardTwoNumber = "**** **** **** 0001";
        int expectedCardOneBalance = 10000;
        int expectedCardTwoBalance = 10000;
        assertEquals(expectedCardOneNumber, cardsIdNumbers[0]);
        assertEquals(expectedCardTwoNumber, cardsIdNumbers[1]);
        assertEquals(expectedCardOneBalance, cardsStrBalance[0]);
        assertEquals(expectedCardTwoBalance, cardsStrBalance[1]);
    }

    @Test
    void
    shouldTransferMoneyInBoundsOfBalance() throws SQLException {
        Dao.clearAuthCodes();
        Dao.setBalanceTo10000();
        login.sendPostRequest("vasya", "qwerty123");
        auth.sendAuthCode("vasya", Dao.getAuthCode(Dao.getId("vasya")));
        String token = auth.getToken("vasya");
        int paymentInRub = 1000;
        Response httpResponse = transfer.transferMoney("5559 0000 0000 0001", "5559 0000 0000 0002", paymentInRub, token);
        int expectedBalanceFromCardInKopecs = 1000000 - paymentInRub * 100;
        int expectedBalanceToCardInKopecs = 1000000 + paymentInRub * 100;
        int expectedStatusCode = 200;
        int actualBalanceFromCardInKopecs = Integer.parseInt(Dao.getCardBalance("5559 0000 0000 0001"));
        int actualBalanceToCardInKopecs = Integer.parseInt(Dao.getCardBalance("5559 0000 0000 0002"));
        assertEquals(expectedBalanceFromCardInKopecs, actualBalanceFromCardInKopecs);
        assertEquals(expectedBalanceToCardInKopecs, actualBalanceToCardInKopecs);
        assertEquals(expectedStatusCode, httpResponse.getStatusCode());
    }

    @Test
    void
    shouldNotTransferMoneyOutOfBoundsOfBalance() throws SQLException {
        Dao.clearAuthCodes();
        Dao.setBalanceTo10000();
        login.sendPostRequest("vasya", "qwerty123");
        auth.sendAuthCode("vasya", Dao.getAuthCode(Dao.getId("vasya")));
        String token = auth.getToken("vasya");
        int paymentInRub = 11000;
        Response httpResponse = transfer.transferMoney("5559 0000 0000 0001", "5559 0000 0000 0002", paymentInRub, token);
        int expectedStatusCode = 400;
        assertEquals(expectedStatusCode, httpResponse.getStatusCode());
    }

    @Test
    void
    shouldTransferMoneyInBoundsOfBalanceDoubleValue() throws SQLException {
        Dao.clearAuthCodes();
        Dao.setBalanceTo10000();
        login.sendPostRequest("vasya", "qwerty123");
        auth.sendAuthCode("vasya", Dao.getAuthCode(Dao.getId("vasya")));
        String token = auth.getToken("vasya");
        double paymentInRub = 1000.5;
        Response httpResponse = transfer.transferMoney("5559 0000 0000 0001", "5559 0000 0000 0002", paymentInRub, token);
        int expectedBalanceFromCardInKopecs = (int) (1000000 - paymentInRub * 100);
        int expectedBalanceToCardInKopecs = (int) (1000000 + paymentInRub * 100);
        int expectedStatusCode = 200;
        int actualBalanceFromCardInKopecs = Integer.parseInt(Dao.getCardBalance("5559 0000 0000 0001"));
        int actualBalanceToCardInKopecs = Integer.parseInt(Dao.getCardBalance("5559 0000 0000 0002"));
        assertEquals(expectedBalanceFromCardInKopecs, actualBalanceFromCardInKopecs);
        assertEquals(expectedBalanceToCardInKopecs, actualBalanceToCardInKopecs);
        assertEquals(expectedStatusCode, httpResponse.getStatusCode());
    }

}

