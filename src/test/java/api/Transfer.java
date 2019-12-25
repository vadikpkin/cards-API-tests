package api;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;

public class Transfer {
    public Response transferMoney(String from, String to, int amount, String token) {
        RestAssured.baseURI = "http://localhost:9999/api";
        RequestSpecification httpRequest = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("from", from);
        requestParams.put("to", to);
        requestParams.put("amount", amount);
        httpRequest.header("Content-Type", "application/json");
        httpRequest.header("Authorization", "Bearer " + token);
        httpRequest.body(requestParams.toJSONString());
        return httpRequest.request(Method.POST, "/transfer");
    }

    public Response transferMoney(String from, String to, double amount, String token) {
        RestAssured.baseURI = "http://localhost:9999/api";
        RequestSpecification httpRequest = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("from", from);
        requestParams.put("to", to);
        requestParams.put("amount", amount);
        httpRequest.header("Content-Type", "application/json");
        httpRequest.header("Authorization", "Bearer " + token);
        httpRequest.body(requestParams.toJSONString());
        return httpRequest.request(Method.POST, "/transfer");
    }
}
