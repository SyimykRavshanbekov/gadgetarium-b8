package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.request.CreatePaymentRequest;
import com.example.gadgetariumb8.db.dto.response.CreatePaymentResponse;
import com.example.gadgetariumb8.db.service.StripeServices;
import org.springframework.stereotype.Service;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public class StripeServicesImpl implements StripeServices {
    @Override
    public CreatePaymentResponse createPaymentIntent(CreatePaymentRequest request) throws StripeException {
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(request.getAmount() * 100L)
                        .setCurrency("kgs")
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods
                                        .builder()
                                        .setEnabled(true)
                                        .build())
                        .build();
        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return new CreatePaymentResponse(paymentIntent.getClientSecret());
    }
}
