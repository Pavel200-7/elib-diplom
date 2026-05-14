package com.example.elib.book.service;

import com.example.elib.book.dto.request.CreateBookDto;
import com.example.elib.book.dto.request.GetBookCriteriaDto;
import com.example.elib.book.dto.request.UpdateBookDto;
import com.example.elib.book.dto.response.BookDto;
import com.example.elib.book.dto.response.BookShortDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface BookService {
    BookDto createBook(CreateBookDto dto);
    BookDto updateBook(UUID id, UpdateBookDto dto);
    BookDto getBookById(UUID id);
    List<BookShortDto> getAllBooks();
    void deleteBook(UUID id);
    Page<BookShortDto> getBooksPage(GetBookCriteriaDto criteria);
}