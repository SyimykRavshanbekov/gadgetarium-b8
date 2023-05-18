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
    public Object payment(@RequestBody OrderPaymentRequest order) {
        try {
            Payment payment = service.createPayment(
                    order.getPrice(),
                    order.getCurrency(),
                    "paypal",
                    "sale",
                    order.getDescription(),
                    "http://localhost:3000/" + CANCEL_URL,
                    "http://localhost:3000/" + SUCCESS_URL
            );
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    System.out.println("link : "+link.getHref());
                    return link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return SimpleResponse.
                builder().
                httpStatus(HttpStatus.BAD_REQUEST)
                .message("Incorrect data entry!!!")
                .build();
    }

    @GetMapping(value = CANCEL_URL)
    public SimpleResponse cancelPay() {
        return SimpleResponse
                .builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Payment Failure")
                .build();
    }

    @GetMapping(value = SUCCESS_URL)
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
