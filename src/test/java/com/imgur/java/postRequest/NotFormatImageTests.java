package com.imgur.java.postRequest;

import com.imgur.java.BaseTest;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class NotFormatImageTests extends BaseTest {

    //    private String uploadedImageId;
    static final String INPUT_IMAGE_FILE_PATH = "notFormat.docx";
    private String fileString;

    @BeforeEach
    void setUp() {
        byte[] fileContent = getFileContent();
        fileString = Base64.getEncoder().encodeToString(fileContent);

    }

    @Test
    @DisplayName("Загрузка не поддерживаемого формата изображения")
    void uploadFileTest() {

        given()
                .headers("Authorization", token)
                .log()
                .all()
                //.multiPart("image", "")
                .multiPart("image", fileString)
                .expect()
                .body("success", is(false))
//                .body("data.error.code", is(1003))
//                .body("data.error.message", is("File type invalid (1)"))
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .statusCode(400);

    }


    private byte[] getFileContent() {
        ClassLoader classLoader = getClass().getClassLoader();
        File inputFile = new File(classLoader.getResource(INPUT_IMAGE_FILE_PATH).getFile());

        byte[] bytes = new byte[0];
        try {
            bytes = FileUtils.readFileToByteArray(inputFile);
            // bytes = FileUtils.readFileToByteArray("src/test/resources/notFormat.mp4");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
