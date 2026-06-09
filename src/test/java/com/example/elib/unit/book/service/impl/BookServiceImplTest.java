package com.example.elib.unit.book.service.impl;

import com.example.elib.author.entity.Author;
import com.example.elib.author.repository.AuthorRepository;
import com.example.elib.book.dto.request.CreateBookDto;
import com.example.elib.book.dto.response.BookDto;
import com.example.elib.book.entity.Book;
import com.example.elib.book.enums.AgeRestrictions;
import com.example.elib.book.mapper.BookMapper;
import com.example.elib.book.repository.BookRepository;
import com.example.elib.book.service.impl.BookServiceImpl;
import com.example.elib.common.exception.DuplicateResourceException;
import com.example.elib.common.exception.ResourceNotFoundException;
import com.example.elib.country.entity.Country;
import com.example.elib.genre.entity.Genre;
import com.example.elib.genre.repository.GenreRepository;
import com.example.elib.language.entity.Language;
import com.example.elib.language.repository.LanguageRepository;
import com.example.elib.literaturegroup.entity.LiteratureGroup;
import com.example.elib.literaturegroup.repository.LiteratureGroupRepository;
import com.example.elib.publishing.entity.Publishing;
import com.example.elib.publishing.repository.PublishingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private LiteratureGroupRepository literatureGroupRepository;

    @Mock
    private PublishingRepository publishingRepository;

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private UUID authorId;
    private UUID genreId;
    private UUID publishingId;
    private UUID languageId;
    private Country country;
    private Author author;
    private Genre genre;
    private Publishing publishing;
    private Language language;
    private Book book;
    private BookDto bookDto;
    private CreateBookDto createBookDto;

    @BeforeEach
    void setUp() {
        authorId = UUID.randomUUID();
        genreId = UUID.randomUUID();
        publishingId = UUID.randomUUID();
        languageId = UUID.randomUUID();
        country = Country.create("Россия");

        author = Author.create("Лев Толстой", country);
        genre = Genre.create("Роман");
        publishing = Publishing.create("Эксмо", "Описание", country);
        language = Language.create("Русский");

        book = Book.create(
                "Война и мир",
                author,
                genre,
                publishing,
                language,
                null,
                1270,
                1869,
                AgeRestrictions.TWELVE_PLUS,
                "Роман-эпопея"
        );

        bookDto = BookDto.builder()
                .id(UUID.randomUUID())
                .name("Война и мир")
                .authorId(authorId)
                .authorName("Лев Толстой")
                .genreId(genreId)
                .genreName("Роман")
                .publishingId(publishingId)
                .publishingName("Эксмо")
                .languageId(languageId)
                .languageName("Русский")
                .pages(1270)
                .publicationYear(1869)
                .ageRestrictions(AgeRestrictions.TWELVE_PLUS)
                .description("Роман-эпопея")
                .build();

        createBookDto = CreateBookDto.builder()
                .name("Война и мир")
                .authorId(authorId)
                .genreId(genreId)
                .publishingId(publishingId)
                .languageId(languageId)
                .literatureGroupId(null)
                .pages(1270)
                .publicationYear(1869)
                .ageRestrictions(AgeRestrictions.TWELVE_PLUS)
                .description("Роман-эпопея")
                .build();
    }

    @Test
    void createBook_Success_ShouldReturnBookDto() {
        // Arrange
        when(bookRepository.existsByName(createBookDto.getName())).thenReturn(false);
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));
        when(publishingRepository.findById(publishingId)).thenReturn(Optional.of(publishing));
        when(languageRepository.findById(languageId)).thenReturn(Optional.of(language));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // Act
        BookDto result = bookService.createBook(createBookDto);

        // Assert
        assertNotNull(result);
        assertEquals(createBookDto.getName(), result.getName());
        assertEquals(authorId, result.getAuthorId());
        assertEquals(genreId, result.getGenreId());

        verify(bookRepository).save(any(Book.class));
        verify(bookMapper).toDto(book);
    }

    @Test
    void createBook_DuplicateName_ShouldThrowDuplicateResourceException() {
        // Arrange
        when(bookRepository.existsByName(createBookDto.getName())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () ->
                bookService.createBook(createBookDto)
        );

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void createBook_AuthorNotFound_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(bookRepository.existsByName(createBookDto.getName())).thenReturn(false);
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                bookService.createBook(createBookDto)
        );

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void createBook_GenreNotFound_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(bookRepository.existsByName(createBookDto.getName())).thenReturn(false);
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(genreRepository.findById(genreId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                bookService.createBook(createBookDto)
        );

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void createBook_PublishingNotFound_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(bookRepository.existsByName(createBookDto.getName())).thenReturn(false);
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));
        when(publishingRepository.findById(publishingId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                bookService.createBook(createBookDto)
        );

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void createBook_LanguageNotFound_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(bookRepository.existsByName(createBookDto.getName())).thenReturn(false);
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));
        when(publishingRepository.findById(publishingId)).thenReturn(Optional.of(publishing));
        when(languageRepository.findById(languageId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                bookService.createBook(createBookDto)
        );

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void createBook_WithLiteratureGroup_Success() {
        // Arrange
        UUID literatureGroupId = UUID.randomUUID();
        LiteratureGroup literatureGroup = LiteratureGroup.create("Художественная литература");
        createBookDto = CreateBookDto.builder()
                .name("Война и мир")
                .authorId(authorId)
                .genreId(genreId)
                .publishingId(publishingId)
                .languageId(languageId)
                .literatureGroupId(literatureGroupId)
                .pages(1270)
                .publicationYear(1869)
                .ageRestrictions(AgeRestrictions.TWELVE_PLUS)
                .description("Роман-эпопея")
                .build();

        when(bookRepository.existsByName(createBookDto.getName())).thenReturn(false);
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));
        when(literatureGroupRepository.findById(literatureGroupId)).thenReturn(Optional.of(literatureGroup));
        when(publishingRepository.findById(publishingId)).thenReturn(Optional.of(publishing));
        when(languageRepository.findById(languageId)).thenReturn(Optional.of(language));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // Act
        BookDto result = bookService.createBook(createBookDto);

        // Assert
        assertNotNull(result);
        verify(literatureGroupRepository).findById(literatureGroupId);
        verify(bookRepository).save(any(Book.class));
    }
}