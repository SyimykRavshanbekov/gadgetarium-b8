package com.example.gadgetariumb8.db.service;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PayPalService {
    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
}
