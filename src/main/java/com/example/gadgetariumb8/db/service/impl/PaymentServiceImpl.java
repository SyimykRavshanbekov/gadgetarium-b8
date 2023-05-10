package com.example.gadgetariumb8.db.service.impl;

import com.example.gadgetariumb8.db.service.PaymentService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private static final String CANCEL_URL = "https://example.com/cancel";
    private static final String RETURN_URL = "https://example.com/return";
    private APIContext context;

    @Override
    public String createPayment(double amount, String description) throws PayPalRESTException {
        String clientId = "Your client Id";
        String clientSecret = "Your Secret";
        Map<String, String> sdkConfig = new HashMap<>();
        sdkConfig.put("mode", "sandbox");
        context = new APIContext(new OAuthTokenCredential(
                clientId, clientSecret, sdkConfig
        ).getAccessToken());

        Amount paymentAmount = new Amount();
        paymentAmount.setCurrency("USD");
        paymentAmount.setTotal(String.format("%.2f", amount));

        Transaction transaction = new Transaction();
        transaction.setAmount(paymentAmount);
        transaction.setDescription(description);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(new RedirectUrls()
                .setCancelUrl(CANCEL_URL)
                .setReturnUrl(RETURN_URL));

        payment = payment.create(context);

        List<Links> links = payment.getLinks();
        for (Links link : links) {
            if (link.getRel().equals("approval_url")){
                return link.getHref();
            }

        }
        return null;
    }

    @Override
    public Payment exicutePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(payerId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return payment.execute(context,paymentExecution);
    }
}
