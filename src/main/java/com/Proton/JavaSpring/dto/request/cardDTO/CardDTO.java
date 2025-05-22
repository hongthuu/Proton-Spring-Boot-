package com.Proton.JavaSpring.dto.request.cardDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CardDTO {
    private Long cardId;
    private String cardType;
    private Date expiryDate;
    private String status;
}
