package com.Proton.JavaSpring.controller;

import com.Proton.JavaSpring.dto.request.PaymentRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    @Autowired
    private ObjectMapper objectMapper;

    @JmsListener(destination = "payment.queue")
    public void receiveMessage(String messageJson) throws JsonProcessingException {
        PaymentRequest request = objectMapper.readValue(messageJson, PaymentRequest.class);
        System.out.println("Payment confirmed for paymentId: " + request.getPaymentId());
    }
}