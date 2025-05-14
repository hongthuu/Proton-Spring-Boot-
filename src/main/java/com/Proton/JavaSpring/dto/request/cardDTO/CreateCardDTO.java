package com.Proton.JavaSpring.dto.request.cardDTO;

import com.Proton.JavaSpring.entity.Account;
import jakarta.persistence.Column;
import lombok.Getter;

import java.util.Date;

@Getter
public class CreateCardDTO {

    private Long accountId;

    private String cardType;

    private Date expiryDate;

    private String status;
}
