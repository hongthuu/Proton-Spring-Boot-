package com.Proton.JavaSpring.controller;

import com.Proton.JavaSpring.dto.request.cardDTO.CreateCardDTO;
import com.Proton.JavaSpring.entity.Card;
import com.Proton.JavaSpring.service.serviceImpl.CardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CardController {
    @Autowired
    private CardServiceImpl cardServiceImpl;

    @GetMapping("/listCard/{id}")
    public ResponseEntity<List<Card>> getListCard(@PathVariable Long id) {
        return ResponseEntity.ok(cardServiceImpl.getListCard(id));
    }

    @PostMapping("/addCard")
    public ResponseEntity<Card> addCard(@RequestBody CreateCardDTO cardDTO) {
        return ResponseEntity.ok(cardServiceImpl.addCard(cardDTO));
    }

    @DeleteMapping("/deleteCard/{id}")
    public ResponseEntity<String> deleteCard(@PathVariable Long id) {
        cardServiceImpl.deleteCard(id);
        return ResponseEntity.ok("Card deleted successfully");
    }
}
