package com.example.gadgetariumb8.db.dto.request;

import java.util.Map;

public record UserOrderRequest(
      Map<Long, Integer> productsAndQuantity
) {
}
