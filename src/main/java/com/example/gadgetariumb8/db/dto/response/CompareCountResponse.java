package com.example.gadgetariumb8.db.dto.response;


import lombok.Builder;

import java.util.Map;
@Builder
public record CompareCountResponse(
        Map<String,Integer> countCompare
) {
}
