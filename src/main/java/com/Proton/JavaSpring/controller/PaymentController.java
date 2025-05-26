package com.Proton.JavaSpring.controller;

import com.Proton.JavaSpring.dto.request.PaymentRequest;
import com.Proton.JavaSpring.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payment")
    public ResponseEntity<String> makePayment(@RequestBody PaymentRequest request) {
        paymentService.sendPayment(request);
        return ResponseEntity.ok("Payment request sent successfully.");
    }
}
