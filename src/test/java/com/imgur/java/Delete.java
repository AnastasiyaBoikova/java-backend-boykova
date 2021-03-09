package com.imgur.java;

import com.imgur.java.dto.Response.Endpoint;
import com.imgur.java.dto.Response.PostImageResponse;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class Delete extends BaseTest {

    private String deleteHash;
    static final String INPUT_IMAGE_FILE_PATH = "bird.jpg";
    private ResponseSpecification responseSpecification;
    private int accountId;

    @BeforeEach
    void setUp() {
        String fileString = new EncoderBase64(INPUT_IMAGE_FILE_PATH).fileString();

        MultiPartSpecification multiPartSpecification = new MultiPartSpecBuilder(fileString)
                .controlName("image")
                .build();
        requestSpecification = requestSpecification
                .multiPart(multiPartSpecification);

        PostImageResponse response = given()
                .spec(requestSpecification)
                .log()
                .all()
                .when()
                .post(Endpoint.POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PostImageResponse.class);
        deleteHash = response.getData().getDeletehash();
      //  accountId = response.getData().getAccountId();

        responseSpecification = new ResponseSpecBuilder()
                .expectBody("success", is(true))
                .expectBody("data", is(true))
                .expectStatusCode(200)
                .build();
    }

    @Test
    @DisplayName("Удаление картинки")
    public void getAccountInfoTest() {

        given()
                .log()
                .all()
                .spec(requestSpecification)
                .when()
                .delete(Endpoint.DELETE_IMAGE_REQUEST, deleteHash)
                .prettyPeek()
                .then()
                .spec(responseSpecification);

    }
}
