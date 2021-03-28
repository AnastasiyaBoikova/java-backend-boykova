package com.imgur.java.postRequest;

import com.imgur.java.BaseTest;
import io.qameta.allure.Attachment;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class NullImageTests extends BaseTest {

      private String uploadedImageId;

    @Epic(value = "Проверка Api Imgur.com")
    @Feature("PostRequest")
    @Flaky
    @Attachment(value = "Вложение", type = "application/json", fileExtension = "null_jpg.jpg")
    @Test
    @DisplayName("Загрузка null изображения ")
    void uploadFileTest() {

        uploadedImageId = given()
                .headers("Authorization", token)
                .log()
                .all()
                .multiPart("image", "R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7")
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.type", is("image/gif"))
                .body("data.height", is(1))
                .body("data.width", is(1))
                .body("data.account_id", is(145270851))
                .statusCode(200)
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }


    @AfterEach
    void tearDown() {

        given()
                .headers("Authorization", token)
                .when()
                .delete("/image/{imageHash}", uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(200);

    }

}
