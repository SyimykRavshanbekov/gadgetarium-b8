package com.example.gadgetariumb8.db.service;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PaymentService {
    String createPayment (double amount,String description) throws PayPalRESTException;
    Payment exicutePayment(String paymentId, String payerId)throws PayPalRESTException;
}
