package com.example.elib.copy.dto.request;

import com.example.elib.common.dto.pagination.PageData;
import com.example.elib.copy.dto.request.pagination.CopySearchCriteria;
import com.example.elib.copy.dto.request.pagination.CopySortCriteria;
import jakarta.validation.Valid;
import lombok.*;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GetCopyCriteriaDto {
    CopySearchCriteria searchCriteria;
    CopySortCriteria sortCriteria;

    @Valid
    PageData pageData;
}
