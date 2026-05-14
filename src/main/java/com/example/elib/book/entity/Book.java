package com.example.elib.book.entity;

import com.example.elib.author.entity.Author;
import com.example.elib.book.enums.AgeRestrictions;
import com.example.elib.common.entity.BaseEntity;
import com.example.elib.genre.entity.Genre;
import com.example.elib.language.entity.Language;
import com.example.elib.literaturegroup.entity.LiteratureGroup;
import com.example.elib.publishing.entity.Publishing;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Year;
import java.util.UUID;

@Entity
@Table(name = "books")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 500)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "literature_group_id")
    private LiteratureGroup literatureGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publishing_id", nullable = false)
    private Publishing publishing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @Column(name = "pages")
    private Integer pages;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "age_restrictions")
    @Enumerated(EnumType.STRING)
    private AgeRestrictions ageRestrictions;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    public static Book create(
            String name,
            Author author,
            Genre genre,
            Publishing publishing,
            Language language,
            LiteratureGroup literatureGroup,
            Integer pages,
            Integer publicationYear,
            AgeRestrictions ageRestrictions,
            String description
    ) {
        Book book = new Book();
        book.name = name;
        book.author = author;
        book.genre = genre;
        book.literatureGroup = literatureGroup;
        book.publishing = publishing;
        book.language = language;
        book.pages = pages;
        book.publicationYear = publicationYear;
        book.ageRestrictions = ageRestrictions;
        book.description = description;
        book.validate();
        return book;
    }

    public void update(
            String name,
            Author author,
            Genre genre,
            Publishing publishing,
            Language language,
            LiteratureGroup literatureGroup,
            Integer pages,
            Integer publicationYear,
            AgeRestrictions ageRestrictions,
            String description
    ) {
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.literatureGroup = literatureGroup;
        this.publishing = publishing;
        this.language = language;
        this.pages = pages;
        this.publicationYear = publicationYear;
        this.ageRestrictions = ageRestrictions;
        this.description = description;
        validate();
    }

    private void validate() {
        validateName();
        validateAuthor();
        validateGenre();
        validatePublishing();
        validateLanguage();
        validatePages();
        validatePublicationYear();
    }

    private void validateName() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название книги не может быть пустым.");
        }
        if (!name.matches("^[A-Za-zА-Яа-яЁё0-9\\s\\-',.!?:;]+$")) {
            throw new IllegalArgumentException(
                    "Название книги может содержать только буквы (русские или английские), " +
                            "цифры, пробелы, дефис, апостроф, запятую, точку, вопросительный и восклицательный знаки, двоеточие и точку с запятой."
            );
        }
        if (name.length() > 500) {
            throw new IllegalArgumentException("Название книги не может превышать 500 символов.");
        }
    }

    private void validateAuthor() {
        if (author == null) {
            throw new IllegalArgumentException("Автор книги обязателен.");
        }
    }

    private void validateGenre() {
        if (genre == null) {
            throw new IllegalArgumentException("Жанр книги обязателен.");
        }
    }

    private void validatePublishing() {
        if (publishing == null) {
            throw new IllegalArgumentException("Издательство книги обязательно.");
        }
    }

    private void validateLanguage() {
        if (language == null) {
            throw new IllegalArgumentException("Язык книги обязателен.");
        }
    }

    private void validatePages() {
        if (pages != null && (pages <= 0 || pages > 10000)) {
            throw new IllegalArgumentException("Количество страниц должно быть от 1 до 10000.");
        }
    }

    private void validatePublicationYear() {
        if (publicationYear != null) {
            int currentYear = Year.now().getValue();
            if (publicationYear < 1450 || publicationYear > currentYear) {
                throw new IllegalArgumentException(
                        "Год публикации должен быть от 1450 (начало книгопечатания) до " + currentYear + "."
                );
            }
        }
    }
}