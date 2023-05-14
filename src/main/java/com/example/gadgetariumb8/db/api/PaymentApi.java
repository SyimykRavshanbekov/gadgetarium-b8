package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.OrderPaymentRequest;
import com.example.gadgetariumb8.db.service.PayPalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/payment")
@RequiredArgsConstructor
@Tag(name = "Payment API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PaymentApi {
    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";
    private final PayPalService service;
    @Value("${server.port}")
    private String port;
    @PostMapping()
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Getting ",
            description = "This method.")
    public Payment payment(@RequestBody OrderPaymentRequest order) throws PayPalRESTException {
        Payment payment = service.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
                order.getIntent(), order.getDescription(), "http://localhost:"+port+"/" + CANCEL_URL,
                "http://localhost:"+port+"/" + SUCCESS_URL);
        return payment;
    }

}
