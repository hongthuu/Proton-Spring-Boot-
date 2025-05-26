package com.Proton.JavaSpring.dto.request.BalanceDTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class BalanceRequest implements Serializable {
    private Long accountId;
    private Double amount;
    private String transactionType;
}
