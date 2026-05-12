package com.example.elib.book.service.impl;

import com.example.elib.author.entity.Author;
import com.example.elib.author.repository.AuthorRepository;
import com.example.elib.book.dto.request.CreateBookDto;
import com.example.elib.book.dto.request.UpdateBookDto;
import com.example.elib.book.dto.response.BookDto;
import com.example.elib.book.dto.response.BookShortDto;
import com.example.elib.book.entity.Book;
import com.example.elib.book.mapper.BookMapper;
import com.example.elib.book.repository.BookRepository;
import com.example.elib.book.service.BookService;
import com.example.elib.common.exeption.DuplicateResourceException;
import com.example.elib.common.exeption.ResourceNotFoundException;
import com.example.elib.genre.entity.Genre;
import com.example.elib.genre.repository.GenreRepository;
import com.example.elib.language.entity.Language;
import com.example.elib.language.repository.LanguageRepository;
import com.example.elib.literaturegroup.entity.LiteratureGroup;
import com.example.elib.literaturegroup.repository.LiteratureGroupRepository;
import com.example.elib.publishing.entity.Publishing;
import com.example.elib.publishing.repository.PublishingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final LiteratureGroupRepository literatureGroupRepository;
    private final PublishingRepository publishingRepository;
    private final LanguageRepository languageRepository;
    private final BookMapper bookMapper;

    @Override
    @Transactional
    public BookDto createBook(CreateBookDto dto) {
        if (bookRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Книга с названием '" + dto.getName() + "' уже существует.");
        }
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Автор с id " + dto.getAuthorId() + " не найден."));
        Genre genre = genreRepository.findById(dto.getGenreId())
                .orElseThrow(() -> new ResourceNotFoundException("Жанр с id " + dto.getGenreId() + " не найден."));
        LiteratureGroup literatureGroup = null;
        if (dto.getLiteratureGroupId() != null) {
            literatureGroup = literatureGroupRepository.findById(dto.getLiteratureGroupId())
                    .orElseThrow(() -> new ResourceNotFoundException("Группа литературы с id " + dto.getLiteratureGroupId() + " не найдена."));
        }
        Publishing publishing = publishingRepository.findById(dto.getPublishingId())
                .orElseThrow(() -> new ResourceNotFoundException("Издательство с id " + dto.getPublishingId() + " не найдено."));
        Language language = languageRepository.findById(dto.getLanguageId())
                .orElseThrow(() -> new ResourceNotFoundException("Язык с id " + dto.getLanguageId() + " не найден."));

        Book book = Book.create(
                dto.getName(),
                author,
                genre,
                publishing,
                language,
                literatureGroup,
                dto.getPages(),
                dto.getPublicationYear(),
                dto.getAgeRestrictions(),
                dto.getDescription()
        );
        book = bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional
    public BookDto updateBook(UUID id, UpdateBookDto dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Книга с id " + id + " не найдена."));
        if (!book.getName().equals(dto.getName()) && bookRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Книга с названием '" + dto.getName() + "' уже существует.");
        }
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Автор с id " + dto.getAuthorId() + " не найден."));
        Genre genre = genreRepository.findById(dto.getGenreId())
                .orElseThrow(() -> new ResourceNotFoundException("Жанр с id " + dto.getGenreId() + " не найден."));
        LiteratureGroup literatureGroup = null;
        if (dto.getLiteratureGroupId() != null) {
            literatureGroup = literatureGroupRepository.findById(dto.getLiteratureGroupId())
                    .orElseThrow(() -> new ResourceNotFoundException("Группа литературы с id " + dto.getLiteratureGroupId() + " не найдена."));
        }
        Publishing publishing = publishingRepository.findById(dto.getPublishingId())
                .orElseThrow(() -> new ResourceNotFoundException("Издательство с id " + dto.getPublishingId() + " не найдено."));
        Language language = languageRepository.findById(dto.getLanguageId())
                .orElseThrow(() -> new ResourceNotFoundException("Язык с id " + dto.getLanguageId() + " не найден."));

        book.update(
                dto.getName(),
                author,
                genre,
                publishing,
                language,
                literatureGroup,
                dto.getPages(),
                dto.getPublicationYear(),
                dto.getAgeRestrictions(),
                dto.getDescription()
        );
        book = bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    public BookDto getBookById(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Книга с id " + id + " не найдена."));
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookShortDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toShortDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteBook(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Книга с id " + id + " не найдена."));

        // TODO: проверить, есть ли ссылки на эту книгу из Copy
        // if (copyRepository.existsByBookId(id)) {
        //     throw new ReferentialIntegrityException("Невозможно удалить книгу, так как есть привязанные экземпляры.");
        // }

        bookRepository.delete(book);
    }
}