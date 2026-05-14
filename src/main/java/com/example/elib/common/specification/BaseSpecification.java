package com.example.elib.common.specification;

import org.springframework.data.jpa.domain.Specification;

public abstract class BaseSpecification {

    protected static <T> Specification<T> distinct() {
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.conjunction();
        };
    }
}