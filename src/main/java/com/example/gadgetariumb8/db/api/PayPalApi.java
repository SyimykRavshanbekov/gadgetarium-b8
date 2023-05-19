package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.request.OrderPaymentRequest;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.service.PayPalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/pay_pal")
@RequiredArgsConstructor
@Tag(name = "Payment API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PayPalApi {

    private final PayPalService service;
    @GetMapping()
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Creating a payment",
            description = "This method creates payment with paypal system.")
    public SimpleResponse successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return SimpleResponse
                        .builder()
                        .httpStatus(HttpStatus.OK)
                        .message("Payment Success")
                        .build();
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return SimpleResponse
                .builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Payment Failure")
                .build();
    }

}
