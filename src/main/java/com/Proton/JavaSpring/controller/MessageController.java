package com.Proton.JavaSpring.controller;

import com.Proton.JavaSpring.entity.User;
import com.Proton.JavaSpring.repository.AccountRepository;
import com.Proton.JavaSpring.service.JmsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JmsService jmsService;

    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody @Valid String userName) {
        User acc = new User();
        acc.setUsername(userName);

        jmsService.sendMessage("user.queue", "Created user: " + acc.getUsername());

        return ResponseEntity.ok("User saved and message sent.");
    }
}

