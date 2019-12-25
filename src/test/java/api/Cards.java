package api;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Cards {
    public String[] getCardsNumbers(String token) {
        RestAssured.baseURI = "http://localhost:9999/api";
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.header("Authorization", "Bearer " + token);
        Response httpResponse = httpRequest.request(Method.GET, "/cards");
        String[] cards = httpResponse.jsonPath().getString("number").replaceAll("[\\[\\]]","").split(", ");
        return cards;
    }

    public int[] getCardsBalance(String token) {
        RestAssured.baseURI = "http://localhost:9999/api";
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.header("Authorization", "Bearer " + token);
        Response httpResponse = httpRequest.request(Method.GET, "/cards");
        String[] cardsBalance = httpResponse.jsonPath().getString("balance").replaceAll("[\\[\\],]", "").split(" ");
        int[] intBalance = new int[cardsBalance.length];
        for (int i = 0; i < cardsBalance.length; i++) {
            intBalance[i] = Integer.parseInt(cardsBalance[i]);
        }
        return intBalance;
    }

}
