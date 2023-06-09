package com.example.gadgetariumb8;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class GadgetariumB8Application {
    @Value("${stripe.api.key}")
    private String stripeApiKey;
    @PostConstruct
    public void setup(){
        Stripe.apiKey = stripeApiKey;
    }

    public static void main(String[] args) {
        SpringApplication.run(GadgetariumB8Application.class, args);
    }

    @GetMapping
    String introduction(){
        return "introduction";
    }

}
