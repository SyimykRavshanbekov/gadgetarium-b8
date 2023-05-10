package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.service.PaymentService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pay/pal/payment")
@Tag(name = "PayPal payment API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PayPalApi {
    private final PaymentService paymentService;

    @GetMapping("/create/payment")
    @Operation(summary = "Create payment", description = "This method for create payment.")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> createPayment(@RequestParam double amount, @RequestParam String description) throws PayPalRESTException {
            String redirectUrl = paymentService.createPayment(amount,description);
            return ResponseEntity.ok().body(redirectUrl);
    }

    @GetMapping("/exicute/payment")
    @Operation(summary = "Execute payment. ", description = "This method for payment integration.")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Payment> executePayment(@RequestParam String paymentId,@RequestParam String  payerId) throws PayPalRESTException {
        Payment payment = paymentService.exicutePayment(paymentId,payerId);
        return ResponseEntity.ok().body(payment);
    }

}
