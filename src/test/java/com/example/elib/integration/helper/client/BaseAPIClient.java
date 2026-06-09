package com.example.elib.integration.helper.client;

import io.restassured.RestAssured;

public abstract class BaseAPIClient {

    public BaseAPIClient(int port) {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }
}