package com.imgur.java;

import com.imgur.java.dto.Response.Endpoint;
import com.imgur.java.dto.Response.GetAccountResponse;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class GetAccountTests extends BaseTest {

    //static private Map<String, String> headers = new HashMap<>();
    private ResponseSpecification responseSpecification;


    @BeforeEach
    void setUp() {
        responseSpecification = new ResponseSpecBuilder()
                .expectBody("success", is(true))
                .expectBody("data.id", is(notNullValue()))
                .expectBody("data.url", is(username))
                .expectBody(CoreMatchers.containsString(username))
                .expectBody("data.created", is(1613117403))
                .expectStatusCode(200)
                .build();

    }

    @Test
    @DisplayName("Получение информации о пользователе.Получение ID")
    public void getAccountInfoTest() {

        GetAccountResponse response = given()
                .log()
                .all()
                .spec(requestSpecification)
                .when()
                .get(Endpoint.GET_ACCOUNT_REQUEST, username)
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .body()
                .as(GetAccountResponse.class);
        prop.setProperty("id", response.getData().getId().toString());

    }

    @Test
    @DisplayName("Неверный username")
    public void getAccountInfoNegativeTest() {

        given()
                .spec(requestSpecification)
                .when()
                .get(Endpoint.GET_ACCOUNT_REQUEST, "testprogmath1")
                .prettyPeek()
                .then()
                .statusCode(404);


    }

}
