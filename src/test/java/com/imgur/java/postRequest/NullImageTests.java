package com.imgur.java.postRequest;

import com.imgur.java.BaseTest;
import com.imgur.java.dto.Response.Endpoint;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class NullImageTests extends BaseTest {

    private String uploadedImageId;
    ResponseSpecification responseSpecification;

    @BeforeEach
    void setUp() {
         responseSpecification = new ResponseSpecBuilder()
                .expectBody("success", is(true))
                .expectBody("data.id", is(notNullValue()))
                .expectBody("data.type", is("image/gif"))
                .expectBody("data.height", is(1))
                .expectBody("data.width", is(1))
                .expectBody("data.account_id", is(145270851))
                .expectStatusCode(200)
                .build();
    }

    @Test
    @DisplayName("Загрузка null изображения ")
    void uploadFileTest() {

        uploadedImageId = given()
                .spec(requestSpecification)
                .log()
                .all()
                .multiPart("image", "R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7")
                .when()
                .post(Endpoint.POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .spec(responseSpecification)
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }


    @AfterEach
    void tearDown() {

        given()
                .spec(requestSpecification)
                .when()
                .delete(Endpoint.DELETE_IMAGE_HASH, uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(200);

    }

}
