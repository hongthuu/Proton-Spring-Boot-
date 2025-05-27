package com.Proton.JavaSpring.service;

import com.Proton.JavaSpring.dto.request.BalanceDTO.BalanceRequest;
import com.Proton.JavaSpring.entity.Balance;

public interface BalanceService {
    Balance getBalance(Long accountId);

    void addBalance(Double balance, Long accId);

    Balance updateBalance(BalanceRequest request);

    void deductMoney(Double balance, Long accId);
}
