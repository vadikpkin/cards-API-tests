package api;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import util.Dao;

import java.sql.SQLException;

public class Auth {

    public Response sendAuthCode(String login, String code) {
        RestAssured.baseURI = "http://localhost:9999/api/auth";
        RequestSpecification httpRequest = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("login", login);
        requestParams.put("code", code);
        httpRequest.header("Content-Type", "application/json");
        httpRequest.body(requestParams.toJSONString());
        return httpRequest.request(Method.POST, "/verification");
    }

    public String getToken(String login) throws SQLException {
        Auth auth = new Auth();
        Response response = auth.sendAuthCode(login, Dao.getAuthCode(Dao.getId(login)));
        return response.jsonPath().getString("token");
    }
}
