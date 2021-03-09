package com.imgur.java.postRequest;

import com.imgur.java.BaseTest;
import com.imgur.java.EncoderBase64;
import com.imgur.java.dto.Response.Endpoint;
import com.imgur.java.dto.Response.PostImageResponse;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class PositiveImageTests extends BaseTest {

    private String uploadedImageId;
    static final String INPUT_IMAGE_FILE_PATH = "bird.jpg";
    private ResponseSpecification responseSpec = null;
    private MultiPartSpecification multiPartSpec = null;

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

        responseSpec = new ResponseSpecBuilder()
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                // .expectHeader("Access-Control-Allow-Credentials", "true")
                .expectBody("success", is(true))
                .expectBody("data.id", is(notNullValue()))
                .expectBody("data.type", is("image/jpeg"))
                .expectBody("data.height", is(505))
                .expectBody("data.width", is(530))
                .expectBody("data.account_id", is(145270851))
                .expectStatusCode(200)
                .build();

    }

    @Test
    @DisplayName("Загрузка изображения 530*505")
    void uploadFileTest() {

        PostImageResponse response = given()
                .spec(requestSpecification)
                .log()
                .all()
                .when()
                .post(Endpoint.POST_IMAGE_REQUEST)
                .prettyPeek()
                .then()
                .spec(responseSpec)
                .extract()
                .body()
                .as(PostImageResponse.class);
        uploadedImageId = response.getData().getId();
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
