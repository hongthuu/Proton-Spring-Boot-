package com.Proton.JavaSpring.dto.request.accountDTO;

import com.Proton.JavaSpring.dto.request.cardDTO.CardDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountDTO {
    private Long accountId;
    private String customerName;
    private List<CardDTO> cards;
}
