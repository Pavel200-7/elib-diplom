package com.example.elib.book.service.impl;

import com.example.elib.author.entity.Author;
import com.example.elib.author.repository.AuthorRepository;
import com.example.elib.book.dto.request.CreateBookDto;
import com.example.elib.book.dto.request.GetBookCriteriaDto;
import com.example.elib.book.dto.request.UpdateBookDto;
import com.example.elib.book.dto.request.pagination.BookSearchCriteria;
import com.example.elib.book.dto.request.pagination.BookSortCriteria;
import com.example.elib.book.dto.request.pagination.PageData;
import com.example.elib.book.dto.response.BookDto;
import com.example.elib.book.dto.response.BookShortDto;
import com.example.elib.book.entity.Book;
import com.example.elib.book.mapper.BookMapper;
import com.example.elib.book.repository.BookRepository;
import com.example.elib.book.repository.spec.BookSpecificationBuilder;
import com.example.elib.book.service.BookService;
import com.example.elib.common.exeption.DuplicateResourceException;
import com.example.elib.common.exeption.ReferentialIntegrityException;
import com.example.elib.common.exeption.ResourceNotFoundException;
import com.example.elib.copy.enums.CopyStatus;
import com.example.elib.copy.repository.CopyRepository;
import com.example.elib.genre.entity.Genre;
import com.example.elib.genre.repository.GenreRepository;
import com.example.elib.language.entity.Language;
import com.example.elib.language.repository.LanguageRepository;
import com.example.elib.literaturegroup.entity.LiteratureGroup;
import com.example.elib.literaturegroup.repository.LiteratureGroupRepository;
import com.example.elib.publishing.entity.Publishing;
import com.example.elib.publishing.repository.PublishingRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    private final CopyRepository copyRepository;
    private final BookSpecificationBuilder specBuilder;



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
         if (copyRepository.existsByBookId(id)) {
             throw new ReferentialIntegrityException("Невозможно удалить книгу, так как есть привязанные экземпляры.");
         }

        bookRepository.delete(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookShortDto> getBooksPage(GetBookCriteriaDto criteria) {
        BookSearchCriteria searchCriteria = criteria.getSearchCriteria();
        BookSortCriteria sortCriteria = criteria.getSortCriteria();
        PageData pageData = criteria.getPageData();

        Specification<Book> spec = specBuilder.fromCriteria(searchCriteria);
        PageRequest pageRequest = buildPageRequest(pageData, sortCriteria);

        Page<Book> bookPage = bookRepository.findAll(spec, pageRequest);
        return bookPage.map(bookMapper::toShortDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getAvailableCount(UUID id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Книга с id " + id + " не найдена.");
        }
        long count = copyRepository.countByBookIdAndStatus(id, CopyStatus.AVAILABLE);
        return (int) count;
    }

    private PageRequest buildPageRequest(PageData pageData, BookSortCriteria sortCriteria) {
        int page = pageData != null ? pageData.getPage() : 0;
        int size = pageData != null && pageData.getSize() > 0 ? pageData.getSize() : 20;

        if (sortCriteria == null || sortCriteria.getSortBy() == null) {
            return PageRequest.of(page, size, Sort.by("name").ascending());
        }

        String sortField = switch (sortCriteria.getSortBy()) {
            case NAME -> "name";
            case PUBLICATION_YEAR -> "publicationYear";
            case CREATED_AT -> "createdAt";
            case UPDATED_AT -> "updatedAt";
        };

        Sort.Direction direction = sortCriteria.getSortDirection() != null
                ? sortCriteria.getSortDirection()
                : Sort.Direction.ASC;

        return PageRequest.of(page, size, Sort.by(direction, sortField));
    }
}