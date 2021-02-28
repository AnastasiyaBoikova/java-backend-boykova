package com.imgur.java;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BaseTest {

    public static Properties prop = new Properties();
    public static String username;
    public static String token;
    public static String id;

    @BeforeAll
    static void beforeAll() {
        loadProperties();
        username = prop.getProperty("username");
        token = prop.getProperty("token");

        //   headers.put("Authorization", token);
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.baseURI = prop.getProperty("base.url");

        RestAssured.filters(new AllureRestAssured());

    }

    private static void loadProperties() {
        try (InputStream app = new FileInputStream("src/test/resources/application.properties")) {
            prop.load(app);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
