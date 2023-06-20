package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.request.CreatePaymentRequest;
import com.example.gadgetariumb8.db.dto.response.CreatePaymentResponse;
import com.stripe.exception.StripeException;
import org.springframework.web.bind.annotation.RequestBody;

public interface StripeServices {
    CreatePaymentResponse createPaymentIntent(CreatePaymentRequest request) throws StripeException;
}
