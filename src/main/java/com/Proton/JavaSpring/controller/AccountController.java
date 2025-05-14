package com.Proton.JavaSpring.controller;

import com.Proton.JavaSpring.dto.request.accountDTO.CreateAccountDTO;
import com.Proton.JavaSpring.dto.request.accountDTO.UpdateAccountDTO;
import com.Proton.JavaSpring.entity.Account;
import com.Proton.JavaSpring.service.serviceImpl.AccountServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AccountController {
    @Autowired
    private AccountServiceImpl accountService;

    @PostMapping("/createAccount")
    public ResponseEntity<Account> createAccount(@RequestBody @Valid CreateAccountDTO accountDTO) {
        return ResponseEntity.ok(accountService.createAccount(accountDTO));
    }

    @PutMapping("/updateAccount")
    public ResponseEntity<Account> updateAccount(@RequestBody @Valid UpdateAccountDTO req) {
        return ResponseEntity.ok(accountService.updateAccount(req));
    }

    @DeleteMapping("/deleteAccount/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok("Account deleted successfully");
    }

    @GetMapping("/getInformation/{id}")
    public ResponseEntity<Account> getInformation(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }
}
