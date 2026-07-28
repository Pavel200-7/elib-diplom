package com.example.elib.user.dto.request.pagination;

import lombok.*;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserSearchCriteria {
    String query;
}
