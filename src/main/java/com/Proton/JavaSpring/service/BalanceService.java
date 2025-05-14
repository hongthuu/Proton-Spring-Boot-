package com.Proton.JavaSpring.service;

import com.Proton.JavaSpring.entity.Balance;

import java.util.List;

public interface BalanceService {
    List<Balance> getBalanceByAccountId(Long accountId);
    void addBalance(Double balance, Long accId);

    void deductMoney(Double balance, Long accId);
}
