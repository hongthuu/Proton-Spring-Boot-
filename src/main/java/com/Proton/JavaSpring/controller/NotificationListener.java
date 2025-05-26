package com.Proton.JavaSpring.controller;

import com.Proton.JavaSpring.dto.request.BalanceDTO.BalanceRequest;
import com.Proton.JavaSpring.dto.request.PaymentRequest;
import com.Proton.JavaSpring.service.BalanceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BalanceService balanceService;

    @JmsListener(destination = "${queue.balance}")
    public void receive(String messageJson) throws JsonProcessingException {
        try {
            BalanceRequest request = objectMapper.readValue(messageJson, BalanceRequest.class);
            balanceService.updateBalance(request);
            log.info("Balance Updated");
        } catch (Exception e) {
            log.info("Failed to process balance update: {}", e.getMessage());
        }
    }

    @JmsListener(destination = "payment.queue")
    public void receiveMessage(String messageJson) throws JsonProcessingException {
        PaymentRequest request = objectMapper.readValue(messageJson, PaymentRequest.class);
        log.info("Payment confirmed for paymentId: {}", request.getPaymentId());
    }
}