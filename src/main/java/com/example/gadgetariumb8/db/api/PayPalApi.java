package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.service.PayPalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/pay_pal")
@RequiredArgsConstructor
@Tag(name = "Payment PayPal API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PayPalApi {

    private final PayPalService service;
    @GetMapping()
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Creating a payment",
            description = "This method creates payment with paypal system.")
    public SimpleResponse successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        return service.successPay(paymentId, payerId);
    }
}
