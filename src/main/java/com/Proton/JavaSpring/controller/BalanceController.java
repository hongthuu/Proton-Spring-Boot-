package com.Proton.JavaSpring.controller;

import com.Proton.JavaSpring.dto.request.BalanceDTO.BalanceRequest;
import com.Proton.JavaSpring.entity.Balance;
import com.Proton.JavaSpring.service.serviceImpl.BalanceServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class BalanceController {
    @Autowired
    private BalanceServiceImpl balanceServiceImpl;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${queue.balance}")
    private String balanceQueue;

    @GetMapping("/getBalance/{accId}")
    public ResponseEntity<Balance> getbalance(@PathVariable Long accId) {
        return ResponseEntity.ok(balanceServiceImpl.getBalance(accId));
    }

    @PostMapping("/addBalance/{accId}")
    public ResponseEntity<String> addBalance(@RequestBody BalanceRequest balanceRequest, @PathVariable Long accId) {
        Double balance1 = balanceRequest.getAmount();
        balanceServiceImpl.addBalance(balance1, accId);
        return ResponseEntity.ok("Balance added successfully");
    }

    @PostMapping("/deductMoney/{accId}")
    public ResponseEntity<String> deductBalance(@RequestBody BalanceRequest balanceRequest, @PathVariable Long accId) {
        Double balance1 = balanceRequest.getAmount();
        balanceServiceImpl.deductMoney(balance1, accId);
        return ResponseEntity.ok("Deduct money successfully");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateBalance(@RequestBody BalanceRequest request) {
        try {
            String json = objectMapper.writeValueAsString(request);
            jmsTemplate.convertAndSend(balanceQueue, json);
            return  ResponseEntity.ok("Balance update request sent to queue.");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON convert error", e);
        }

    }
}
