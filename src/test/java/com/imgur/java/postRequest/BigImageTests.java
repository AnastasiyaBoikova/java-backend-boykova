package com.imgur.java.postRequest;

import com.imgur.java.BaseTest;
import io.qameta.allure.*;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class BigImageTests extends BaseTest {

    static final String INPUT_IMAGE_FILE_PATH = "Big.png";
    private String fileString;

    @BeforeEach
    void setUp() {
        byte[] fileContent = getFileContent();
        fileString = Base64.getEncoder().encodeToString(fileContent);
    }
    @Epic(value = "Проверка Api Imgur.com")
    @Feature ("PostRequest")
    @Flaky
    @Test
    @DisplayName("Загрузка тяжелого изображения")
    @Attachment (value = "Вложение", type = "application/json", fileExtension = "Big.png")
    void uploadFileTest() {


        given()
                .headers("Authorization", token)
                .log()
                .all()
//              .multiPart("image", "/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAIBAQIBAQICAgICAgICAwUDAwMDAwYEBAMFBwYHBwcGBwcICQsJCAgKCAcHCg0KCgsMDAwMBwkODw0MDgsMDAz/2wBDAQICAgMDAwYDAwYMCAcIDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAz/wAARCAABAAEDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD9/KKKKAP/2Q==")
                .multiPart("image", fileString)
                .expect()
                .body("success", is(false))
                .body("data.error", is("File is over the size limit"))
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
            //           bytes = FileUtils.readFileToByteArray("src/test/resources/bird.jpg"); - абсолютный путь. 66-71 не нужно было бы
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
