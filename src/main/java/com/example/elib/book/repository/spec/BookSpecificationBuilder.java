package com.example.elib.book.repository.spec;

import com.example.elib.book.dto.request.pagination.BookSearchCriteria;
import com.example.elib.book.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public interface BookSpecificationBuilder {
    Specification<Book> fromCriteria(BookSearchCriteria criteria);
}