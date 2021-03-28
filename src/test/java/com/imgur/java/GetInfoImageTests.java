package com.imgur.java;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import org.apache.commons.io.FileUtils;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class GetInfoImageTests extends BaseTest {

    //static private Map<String, String> headers = new HashMap<>();
    private String uploadedImageId;
    static final String INPUT_IMAGE_FILE_PATH = "bird.jpg";
    private String fileString;

    @BeforeEach
    void setUp() {
        byte[] fileContent = getFileContent();
        fileString = Base64.getEncoder().encodeToString(fileContent);
        uploadedImageId = given()
                .headers("Authorization", token)
                .log()
                .all()
                .multiPart("image", fileString)
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }

    @Epic(value = "Проверка Api Imgur.com")
    @Feature("Get Image")
    @Flaky
    @Test
    @DisplayName("Получение информации о картинки")
    public void getAccountInfoTest() {

        given()
                .log()
                .all()
                .headers("Authorization", token)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.type", CoreMatchers.is("image/jpeg"))
                .body("data.height", CoreMatchers.is(505))
                .body("data.width", CoreMatchers.is(530))
                .body("data.account_id", CoreMatchers.is(145270851))
                .statusCode(200)
                .when()
                .get("/image/{imageHash}",   uploadedImageId)
                .prettyPeek()
                .then();

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

    private byte[] getFileContent() {
        ClassLoader classLoader = getClass().getClassLoader();
        File inputFile = new File(classLoader.getResource(INPUT_IMAGE_FILE_PATH).getFile());

        byte[] bytes = new byte[0];
        try {
            bytes = FileUtils.readFileToByteArray(inputFile);
            //           bytes = FileUtils.readFileToByteArray("src/test/resources/bird.jpg"); - абсолютный путь. 66-71 не нужно было бы
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

}
