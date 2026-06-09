package com.example.elib.integration.helper.client;

import com.example.elib.book.dto.request.CreateBookDto;
import com.example.elib.book.dto.response.BookDto;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static io.restassured.RestAssured.given;

@SpringBootTest
public class BookAPIClient extends BaseAPIClient {

    public BookAPIClient(int port) {
        super(port);
    }

    public BookDto createBook(CreateBookDto request) {
        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/books")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .as(BookDto.class);
    }
}