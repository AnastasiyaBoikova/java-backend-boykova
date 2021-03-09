package com.imgur.java;

import com.imgur.java.dto.Response.Endpoint;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class GetInfoImageTests extends BaseTest {

    //static private Map<String, String> headers = new HashMap<>();
    private String uploadedImageId;
    static final String INPUT_IMAGE_FILE_PATH = "bird.jpg";
    private String fileString;
    private MultiPartSpecification multiPartSpecification;
    private ResponseSpecification responseSpecification;

    @BeforeEach
    void setUp() {
        fileString = new EncoderBase64(INPUT_IMAGE_FILE_PATH).fileString();

        multiPartSpecification = new MultiPartSpecBuilder(fileString)
                .controlName("image")
                .build();

        requestSpecification = requestSpecification
                .multiPart(multiPartSpecification);


        uploadedImageId = given()
                .spec(requestSpecification)
                .log()
                .all()
                .when()
                .post("/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");

        responseSpecification = new ResponseSpecBuilder()
                .expectBody("success", is(true))
                .expectBody("data.id", is(notNullValue()))
                .expectBody("data.type", CoreMatchers.is("image/jpeg"))
                .expectBody("data.height", CoreMatchers.is(505))
                .expectBody("data.width", CoreMatchers.is(530))
                .expectBody("data.account_id", CoreMatchers.is(145270851))
                .expectStatusCode(200)
                .build();
    }

    @Test
    @DisplayName("Получение информации о картинки")
    public void getAccountInfoTest() {

        given()
                .log()
                .all()
                .spec(requestSpecification)
                .when()
                .get(Endpoint.GET_IMAGE_REQUEST, uploadedImageId)
                .prettyPeek()
                .then()
                .spec(responseSpecification);
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
