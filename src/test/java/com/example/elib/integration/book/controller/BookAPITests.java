package com.example.elib.integration.book.controller;

import com.example.elib.author.entity.Author;
import com.example.elib.book.dto.request.CreateBookDto;
import com.example.elib.book.dto.response.BookDto;
import com.example.elib.book.enums.AgeRestrictions;
import com.example.elib.country.entity.Country;
import com.example.elib.genre.entity.Genre;
import com.example.elib.integration.helper.cleaner.DatabaseCleaner;
import com.example.elib.integration.helper.client.BookAPIClient;
import com.example.elib.integration.helper.initializer.*;
import com.example.elib.language.entity.Language;
import com.example.elib.publishing.entity.Publishing;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BookAPITests {

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @LocalServerPort
    protected int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private CountryInitializer countryInitializer;

    @Autowired
    private AuthorInitializer authorInitializer;

    @Autowired
    private GenreInitializer genreInitializer;

    @Autowired
    private PublishingInitializer publishingInitializer;

    @Autowired
    private LanguageInitializer languageInitializer;

    private BookAPIClient bookAPIClient;

    @Container
    protected static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> "");
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri", () -> "");
    }

    @BeforeEach
    protected void setUp() {
        bookAPIClient = new BookAPIClient(port);
        databaseCleaner.cleanAll();
    }

    @Test
    public void ShouldCreateBook() {
        // Arrange
        Country country = countryInitializer.createCountry("Россия");
        Author author = authorInitializer.createAuthor("Лев Толстой", country);
        Genre genre = genreInitializer.createGenre("Роман");
        Publishing publishing = publishingInitializer.createPublishing("Эксмо", country);
        Language language = languageInitializer.createLanguage("Русский");
        CreateBookDto request = CreateBookDto.builder()
                .name("Война и мир")
                .authorId(author.getId())
                .genreId(genre.getId())
                .publishingId(publishing.getId())
                .languageId(language.getId())
                .literatureGroupId(null)
                .pages(1270)
                .publicationYear(1869)
                .ageRestrictions(AgeRestrictions.TWELVE_PLUS)
                .description("Роман-эпопея")
                .build();

        // Act
        BookDto response = bookAPIClient.createBook(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(request.getName(), response.getName());
        assertEquals(author.getId(), response.getAuthorId());
        assertEquals(author.getName(), response.getAuthorName());
        assertEquals(genre.getId(), response.getGenreId());
        assertEquals(genre.getName(), response.getGenreName());
        assertEquals(publishing.getId(), response.getPublishingId());
        assertEquals(publishing.getName(), response.getPublishingName());
        assertEquals(language.getId(), response.getLanguageId());
        assertEquals(language.getName(), response.getLanguageName());
        assertEquals(request.getPages(), response.getPages());
        assertEquals(request.getPublicationYear(), response.getPublicationYear());
        assertEquals(request.getAgeRestrictions(), response.getAgeRestrictions());
    }

    @Test
    public void ShouldReturn400WhenNameIsBlank() {
        // Arrange
        Country country = countryInitializer.createCountry("Россия");
        Author author = authorInitializer.createAuthor("Лев Толстой", country);
        Genre genre = genreInitializer.createGenre("Роман");
        Publishing publishing = publishingInitializer.createPublishing("Эксмо", country);
        Language language = languageInitializer.createLanguage("Русский");

        CreateBookDto request = CreateBookDto.builder()
                .name("")
                .authorId(author.getId())
                .genreId(genre.getId())
                .publishingId(publishing.getId())
                .languageId(language.getId())
                .literatureGroupId(null)
                .pages(1270)
                .publicationYear(1869)
                .ageRestrictions(AgeRestrictions.TWELVE_PLUS)
                .description("Роман-эпопея")
                .build();

        // Act & Assert
        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/v1/books")
                .then()
                .statusCode(400);
    }

    @Test
    public void ShouldReturn409WhenDuplicateName() {
        // Arrange
        Country country = countryInitializer.createCountry("Россия");
        Author author = authorInitializer.createAuthor("Лев Толстой", country);
        Genre genre = genreInitializer.createGenre("Роман");
        Publishing publishing = publishingInitializer.createPublishing("Эксмо", country);
        Language language = languageInitializer.createLanguage("Русский");

        CreateBookDto request = CreateBookDto.builder()
                .name("Война и мир")
                .authorId(author.getId())
                .genreId(genre.getId())
                .publishingId(publishing.getId())
                .languageId(language.getId())
                .literatureGroupId(null)
                .pages(1270)
                .publicationYear(1869)
                .ageRestrictions(AgeRestrictions.TWELVE_PLUS)
                .description("Роман-эпопея")
                .build();

        // Act
        bookAPIClient.createBook(request);

        // Assert - повторное создание должно вернуть 409 Conflict
        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/v1/books")
                .then()
                .statusCode(409);
    }

    @Test
    public void ShouldReturn404WhenAuthorNotFound() {
        // Arrange
        Country country = countryInitializer.createCountry("Россия");
        Genre genre = genreInitializer.createGenre("Роман");
        Publishing publishing = publishingInitializer.createPublishing("Эксмо", country);
        Language language = languageInitializer.createLanguage("Русский");

        CreateBookDto request = CreateBookDto.builder()
                .name("Война и мир")
                .authorId(UUID.randomUUID())
                .genreId(genre.getId())
                .publishingId(publishing.getId())
                .languageId(language.getId())
                .literatureGroupId(null)
                .pages(1270)
                .publicationYear(1869)
                .ageRestrictions(AgeRestrictions.TWELVE_PLUS)
                .description("Роман-эпопея")
                .build();

        // Act & Assert
        RestAssured.given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/api/v1/books")
                .then()
                .statusCode(404);
    }
}