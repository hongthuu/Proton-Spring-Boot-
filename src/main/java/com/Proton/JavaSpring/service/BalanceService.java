package com.Proton.JavaSpring.service;

import com.Proton.JavaSpring.dto.request.BalanceDTO.BalanceRequest;
import com.Proton.JavaSpring.entity.Balance;

import java.util.List;

public interface BalanceService {
    Balance getBalance(Long accountId);

    void addBalance(Double balance, Long accId);

    void updateBalance(BalanceRequest request);

    void deductMoney(Double balance, Long accId);
}
