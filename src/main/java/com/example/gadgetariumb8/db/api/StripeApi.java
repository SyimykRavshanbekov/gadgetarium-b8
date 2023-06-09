package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.CreatePaymentRequest;
import com.example.gadgetariumb8.db.dto.response.CreatePaymentResponse;
import com.example.gadgetariumb8.db.service.StripeServices;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/stripe")
@RequiredArgsConstructor
@Tag(name = "Payment Stripe API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StripeApi {
    private final StripeServices stripeServices;
    @Value("${stripe.public.key}")
    private String stripePublicKey;
    @PostMapping("/create-payment-intent")
    @Operation(summary = "Creating a payment",
            description = "This method creates payment with stripe system.")
    public CreatePaymentResponse createPaymentIntent(@RequestBody CreatePaymentRequest request) throws StripeException{
        return stripeServices.createPaymentIntent(request);
    }

    @GetMapping("/public_key")
    public String key(){
        return stripePublicKey;
    }

}
