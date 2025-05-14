package com.Proton.JavaSpring.service;

import com.Proton.JavaSpring.dto.request.cardDTO.CreateCardDTO;
import com.Proton.JavaSpring.entity.Card;

import java.util.List;

public interface CardService {
    List<Card> getListCard(Long id);

    Card addCard(CreateCardDTO cardDTO);

    void deleteCard(Long id);
}
