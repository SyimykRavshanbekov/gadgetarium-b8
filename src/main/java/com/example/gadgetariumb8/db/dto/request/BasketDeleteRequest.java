package com.example.gadgetariumb8.db.dto.request;

import java.util.List;

/**
 * @author kurstan
 * @created at 01.06.2023 16:52
 */
public record BasketDeleteRequest(
        List<Long> subProductsId
) {
}
