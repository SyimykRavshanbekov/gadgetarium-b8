package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.request.DiscountRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;

public interface DiscountService {
    SimpleResponse addDiscount(DiscountRequest discountRequest);
}
