package api;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;

public class Login {

    public Response sendPostRequest(String login, String pass) {
        RestAssured.baseURI = "http://localhost:9999/api/";
        RequestSpecification httpRequest = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("login", login);
        requestParams.put("password", pass);
        httpRequest.header("Content-Type", "application/json");
        httpRequest.body(requestParams.toJSONString());
        return httpRequest.request(Method.POST, "/auth");
    }

}

