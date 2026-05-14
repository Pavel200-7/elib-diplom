package com.example.elib.book.repository.spec;

import com.example.elib.book.dto.request.pagination.BookSearchCriteria;
import com.example.elib.book.entity.Book;
import com.example.elib.book.enums.AgeRestrictions;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BookSpecificationBuilderImpl implements BookSpecificationBuilder {

    @Override
    public Specification<Book> fromCriteria(BookSearchCriteria criteria) {
        if (criteria == null) {
            return Specification.where((Specification<Book>) null);
        }

        return Specification.where(byName(criteria.getName()))
                .and(byAuthorId(criteria.getAuthorId()))
                .and(byGenreId(criteria.getGenreId()))
                .and(byLiteratureGroupId(criteria.getLiteratureGroupId()))
                .and(byPublishingId(criteria.getPublishingId()))
                .and(byLanguageId(criteria.getLanguageId()))
                .and(byPagesRange(criteria.getPagesMin(), criteria.getPagesMax()))
                .and(byPublicationYearRange(criteria.getPublicationYearMin(), criteria.getPublicationYearMax()))
                .and(byAgeRestrictions(criteria.getAgeRestrictions()));
    }


    private Specification<Book> byName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    private Specification<Book> byAuthorId(UUID authorId) {
        return (root, query, cb) -> {
            if (authorId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("author").get("id"), authorId);
        };
    }

    private Specification<Book> byGenreId(UUID genreId) {
        return (root, query, cb) -> {
            if (genreId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("genre").get("id"), genreId);
        };
    }

    private Specification<Book> byLiteratureGroupId(UUID literatureGroupId) {
        return (root, query, cb) -> {
            if (literatureGroupId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("literatureGroup").get("id"), literatureGroupId);
        };
    }

    private Specification<Book> byPublishingId(UUID publishingId) {
        return (root, query, cb) -> {
            if (publishingId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("publishing").get("id"), publishingId);
        };
    }

    private Specification<Book> byLanguageId(UUID languageId) {
        return (root, query, cb) -> {
            if (languageId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("language").get("id"), languageId);
        };
    }

    private Specification<Book> byPagesRange(Integer min, Integer max) {
        return (root, query, cb) -> {
            if (min == null && max == null) {
                return cb.conjunction();
            }
            if (min == null) {
                return cb.lessThanOrEqualTo(root.get("pages"), max);
            }
            if (max == null) {
                return cb.greaterThanOrEqualTo(root.get("pages"), min);
            }
            return cb.between(root.get("pages"), min, max);
        };
    }

    private Specification<Book> byPublicationYearRange(Integer min, Integer max) {
        return (root, query, cb) -> {
            if (min == null && max == null) {
                return cb.conjunction();
            }
            if (min == null) {
                return cb.lessThanOrEqualTo(root.get("publicationYear"), max);
            }
            if (max == null) {
                return cb.greaterThanOrEqualTo(root.get("publicationYear"), min);
            }
            return cb.between(root.get("publicationYear"), min, max);
        };
    }

    private Specification<Book> byAgeRestrictions(AgeRestrictions ageRestrictions) {
        return (root, query, cb) -> {
            if (ageRestrictions == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("ageRestrictions"), ageRestrictions);
        };
    }
}