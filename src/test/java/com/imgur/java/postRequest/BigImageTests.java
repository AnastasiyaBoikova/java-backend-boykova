package com.imgur.java.postRequest;

import com.imgur.java.BaseTest;
import com.imgur.java.EncoderBase64;
import com.imgur.java.dto.Response.Endpoint;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class BigImageTests extends BaseTest {

    static final String INPUT_IMAGE_FILE_PATH = "Big.png";
    private MultiPartSpecification multiPartSpecification = null;
    private ResponseSpecification responseSpecification = null;

    @BeforeEach
    void setUp() {
        String fileString = new EncoderBase64(INPUT_IMAGE_FILE_PATH).fileString();
        multiPartSpecification = new MultiPartSpecBuilder(fileString)
                .controlName("image")
                .build();

        requestSpecification = requestSpecification
                .multiPart(multiPartSpecification);

        responseSpecification = new ResponseSpecBuilder()
                .expectBody("success", is(false))
                .expectBody("data.error", is("File is over the size limit"))
                .expectStatusCode(400)
                .build();
    }

    @Test
    @DisplayName("Загрузка тяжелого изображения")
    void uploadFileTest() {

        given()
                .spec(requestSpecification)
                .log()
                .all()
                .when()
                .post(Endpoint.POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .spec(responseSpecification);
    }
}
