package com.imgur.java;

import com.imgur.java.dto.Response.Endpoint;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class AddFavorites extends BaseTest {


    private String uploadedImageId;
    static final String INPUT_IMAGE_FILE_PATH = "bird.jpg";
    MultiPartSpecification multiPartSpec = null;
    ResponseSpecification responseSpecification = null;


    @BeforeEach
    void setUp() {
        String fileString = new EncoderBase64(INPUT_IMAGE_FILE_PATH).fileString();

        // Multi-part запросов
        multiPartSpec = new MultiPartSpecBuilder(fileString)
                .controlName("image")
                .build();

// Расширить спецификацию запроса
        requestSpecification = requestSpecification
                .multiPart(multiPartSpec);

        uploadedImageId = given()
                .spec(requestSpecification)
                .log()
                .all()
                .when()
                .post(Endpoint.POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");

        responseSpecification = new ResponseSpecBuilder()
                .expectBody("success", is(true))
                .expectBody("data", is("favorited"))
                .expectStatusCode(200)
                .build();

    }

    @Test
    @DisplayName("Добавление картинки в избранное")
    public void getAccountInfoTest() {

        given()
                .log()
                .all()
                .spec(requestSpecification)
                .when()
                .post(Endpoint.POST_IMAGE_FAVORITE, uploadedImageId)
                .prettyPeek();
    }

    @AfterEach
    void tearDown() {

        given()
                .spec(requestSpecification)
                .when()
                .delete(Endpoint.DELETE_IMAGE_REQUEST, uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
