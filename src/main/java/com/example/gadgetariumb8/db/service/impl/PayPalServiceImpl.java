package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.service.PayPalService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayPalServiceImpl implements PayPalService {
    @Autowired
    private APIContext apiContext;
    @Override
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }
    @Override
    public SimpleResponse successPay(String paymentId,String payerId) {
        try {
            Payment payment = executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return SimpleResponse
                        .builder()
                        .httpStatus(HttpStatus.OK)
                        .message("Успешный платеж!!")
                        .build();
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return SimpleResponse
                .builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Ошибка платежа!!")
                .build();
    }

}
