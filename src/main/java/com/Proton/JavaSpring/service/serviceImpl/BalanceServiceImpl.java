package com.Proton.JavaSpring.service.serviceImpl;

import com.Proton.JavaSpring.entity.Balance;
import com.Proton.JavaSpring.repository.AccountRepository;
import com.Proton.JavaSpring.repository.BalanceRepository;
import com.Proton.JavaSpring.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BalanceServiceImpl implements BalanceService {
    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Balance> getBalanceByAccountId(Long accountId) {
        if(!accountRepository.existsById(accountId)) {
           throw new RuntimeException("Account not found");
        }
        return balanceRepository.getBalancesByAccountId(accountId);
    }

    @Override
    public void addBalance(Double balance, Long accId) {
        if(!accountRepository.existsById(accId)) {
            throw new RuntimeException("Account not found");
        }
        Balance currentBalance = balanceRepository.findById(accId).get();
        currentBalance.setAvailableBalance(currentBalance.getAvailableBalance() + balance);
        balanceRepository.saveAndFlush(currentBalance);
    }

    @Override
    public void deductMoney(Double balance, Long accId) {
        if(!accountRepository.existsById(accId)) {
            throw new RuntimeException("Account not found");
        }
        Balance currentBalance = balanceRepository.findById(accId).get();
        if(currentBalance.getAvailableBalance() < balance) {
            throw new RuntimeException("Insufficient balance");
        }
        currentBalance.setAvailableBalance(currentBalance.getAvailableBalance() - balance);
        balanceRepository.saveAndFlush(currentBalance);
    }


}
