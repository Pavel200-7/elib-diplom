package com.example.elib.copy.repository.spec;

import com.example.elib.copy.dto.request.pagination.CopySearchCriteria;
import com.example.elib.copy.entity.Copy;
import org.springframework.data.jpa.domain.Specification;

public interface CopySpecificationBuilder {
    Specification<Copy> fromCriteria(CopySearchCriteria criteria);
}
