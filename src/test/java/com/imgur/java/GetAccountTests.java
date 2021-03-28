package com.imgur.java;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Step;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class GetAccountTests extends BaseTest {

    //static private Map<String, String> headers = new HashMap<>();
    @Epic(value = "Проверка Api Imgur.com")
    @Feature("Get Account")
    @Flaky
    @Test
    @Step ("Positive")
    @DisplayName("Получение информации о пользователе.Получение ID")
    public void getAccountInfoTest() {

        id = given()
                .log()
                .all()
                .headers("Authorization", token)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.url", is(username))
                .body(CoreMatchers.containsString(username))
                .body("data.created", is(1613117403))
                .statusCode(200)
                .when()
                .get("/account/{username}", username)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
//                .getString("data.url");
//        assertThat(url, equalTo(username));
                .getString("data.id");

    }
    @Epic(value = "Проверка Api Imgur.com")
    @Feature("Get Account")
    @Flaky
    @Step ("NegativeTest")
    @Test
    @DisplayName("Неверный username")
    public void getAccountInfoNegativeTest() {

        given()
                .headers("Authorization", token)
                .when()
                .get("https://api.imgur.com/3/account/{username}", "testprogmath1")
                .prettyPeek()
                .then()
                .statusCode(404);


    }

}
