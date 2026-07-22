package com.example.elib.copy.repository.spec;

import com.example.elib.copy.dto.request.pagination.CopySearchCriteria;
import com.example.elib.copy.entity.Copy;
import com.example.elib.copy.enums.CopyStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CopySpecificationBuilderImpl implements CopySpecificationBuilder {

    @Override
    public Specification<Copy> fromCriteria(CopySearchCriteria criteria) {
        if (criteria == null) {
            return Specification.where((Specification<Copy>) null);
        }

        return Specification.where(byInventoryNumber(criteria.getInventoryNumber()))
                .and(byIsbn(criteria.getIsbn()))
                .and(byHolderId(criteria.getHolderId()))
                .and(byBookId(criteria.getBookId()))
                .and(byStatus(criteria.getStatus()));
    }

    private Specification<Copy> byInventoryNumber(String inventoryNumber) {
        return (root, query, cb) -> {
            if (inventoryNumber == null || inventoryNumber.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("inventoryNumber")), "%" + inventoryNumber.toLowerCase() + "%");
        };
    }

    private Specification<Copy> byIsbn(String isbn) {
        return (root, query, cb) -> {
            if (isbn == null || isbn.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("isbn")), "%" + isbn.toLowerCase() + "%");
        };
    }

    private Specification<Copy> byHolderId(UUID holderId) {
        return (root, query, cb) -> {
            if (holderId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("holder").get("id"), holderId);
        };
    }

    private Specification<Copy> byBookId(UUID bookId) {
        return (root, query, cb) -> {
            if (bookId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("book").get("id"), bookId);
        };
    }

    private Specification<Copy> byStatus(CopyStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }
}