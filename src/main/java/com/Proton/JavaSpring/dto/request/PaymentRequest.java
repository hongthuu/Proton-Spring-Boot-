package com.Proton.JavaSpring.dto.request;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Getter
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentRequest implements Serializable {
    private String paymentId;
    private String accountId;
    private double amount;
    private String currency;
}