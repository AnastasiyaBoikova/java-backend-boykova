package com.imgur.java;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class Delete extends BaseTest {


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


    @Test
    @DisplayName("Удаление картинки")
    public void getAccountInfoTest() {

        given()
                .log()
                .all()
                .headers("Authorization", token)
                .expect()
                .body("success", is(true))
                .body("data", is(true))
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
