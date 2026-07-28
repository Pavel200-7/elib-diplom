package com.example.elib.user.dto.request;

import com.example.elib.common.dto.pagination.PageData;
import com.example.elib.user.dto.request.pagination.UserSearchCriteria;
import jakarta.validation.Valid;
import lombok.*;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GetUserCriteriaDto {
    UserSearchCriteria searchCriteria;

    @Valid
    PageData pageData;
}
