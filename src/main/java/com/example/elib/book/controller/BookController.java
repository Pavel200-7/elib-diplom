package com.example.elib.book.controller;

import com.example.elib.book.dto.request.CreateBookDto;
import com.example.elib.book.dto.request.GetBookCriteriaDto;
import com.example.elib.book.dto.request.UpdateBookDto;
import com.example.elib.book.dto.response.BookDto;
import com.example.elib.book.dto.response.BookShortDto;
import com.example.elib.book.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody CreateBookDto dto) {
        BookDto created = bookService.createBook(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable UUID id, @RequestBody UpdateBookDto dto) {
        BookDto updated = bookService.updateBook(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable UUID id) {
        BookDto book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<List<BookShortDto>> getAllBooks() {
        List<BookShortDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/page")
    public ResponseEntity<Page<BookShortDto>> getBooksPage(@Valid @RequestBody GetBookCriteriaDto criteria) {
        Page<BookShortDto> result = bookService.getBooksPage(criteria);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/available/count")
    public ResponseEntity<Integer> getAvailableCount(@PathVariable UUID id) {
        Integer result = bookService.getAvailableCount(id);
        return ResponseEntity.ok(result);
    }
}