package com.example.gadgetariumb8.db.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PaginationResponse<T>(
        int countOfElements,
        List<T> elements,
        int currentPage,
        int totalPages
) {
}
