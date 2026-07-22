package com.example.elib.user.dto.request.pagination;

import com.example.elib.user.enums.UserSortField;
import lombok.*;
import org.springframework.data.domain.Sort;

/**
 * Критерии сортировки пользователей
 */
@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserSortCriteria {
    UserSortField sortBy;
    Sort.Direction sortDirection;
}
