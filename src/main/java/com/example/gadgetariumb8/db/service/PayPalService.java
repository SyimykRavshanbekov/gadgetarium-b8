package com.example.gadgetariumb8.db.service;

import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PayPalService {
    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
    public SimpleResponse successPay(String paymentId, String payerId);
}
