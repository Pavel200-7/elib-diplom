package com.example.elib.copy.service.impl.utils;

import com.example.elib.common.dto.pagination.PageData;
import com.example.elib.copy.dto.request.pagination.CopySortCriteria;
import com.example.elib.copy.enums.CopySortField;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public final class CopyPageRequestUtils {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final String DEFAULT_SORT_FIELD = CopySortField.INVENTORY_NUMBER.getFieldName();

    private CopyPageRequestUtils() {
        throw new UnsupportedOperationException("Static Utility class cannot be instantiated");
    }

    public static PageRequest buildPageRequest(PageData pageData, CopySortCriteria sortCriteria) {
        int page = pageData != null ? pageData.getPage() : 0;
        int size = pageData != null && pageData.getSize() > 0 ? pageData.getSize() : DEFAULT_PAGE_SIZE;

        if (sortCriteria == null || sortCriteria.getSortBy() == null) {
            return PageRequest.of(page, size, Sort.by(DEFAULT_SORT_FIELD).ascending());
        }

        String sortField = sortCriteria.getSortBy()
                .getFieldName();

        Sort.Direction direction = sortCriteria.getSortDirection() != null
                ? sortCriteria.getSortDirection()
                : Sort.Direction.ASC;

        return PageRequest.of(page, size, Sort.by(direction, sortField));
    }
}
