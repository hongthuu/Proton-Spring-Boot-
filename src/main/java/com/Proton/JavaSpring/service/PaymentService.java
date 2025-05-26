package com.Proton.JavaSpring.service;

import com.Proton.JavaSpring.dto.request.PaymentRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendPayment(PaymentRequest request) {
        try {
            String json = objectMapper.writeValueAsString(request);
            jmsTemplate.convertAndSend("payment.queue", json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON convert error", e);
        }
    }

}
