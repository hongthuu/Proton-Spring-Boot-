package com.Proton.JavaSpring.service.serviceImpl;

import com.Proton.JavaSpring.entity.Account;
import com.Proton.JavaSpring.entity.Balance;
import com.Proton.JavaSpring.repository.AccountRepository;
import com.Proton.JavaSpring.repository.BalanceRepository;
import com.Proton.JavaSpring.service.BalanceService;
import com.Proton.JavaSpring.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {
    private final RedisService<Balance> redisService;
    private final BalanceRepository balanceRepository;
    private final AccountRepository accountRepository;

    private static final Duration BALANCE_CACHE_DURATION = Duration.ofMinutes(10);
    private static final String BALANCE_KEY_PREFIX = "balance:";

    @Override
    public Balance getBalance(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new RuntimeException("Account not found");
        }

        Balance balance = redisService.get(BALANCE_KEY_PREFIX, accountId, Balance.class);
        if (balance != null) {
            return balance;
        }
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        balance = account.getBalance();

        redisService.save(BALANCE_KEY_PREFIX, accountId, balance, BALANCE_CACHE_DURATION);
        return balance;
    }


    @Override
    public void addBalance(Double amount, Long accId) {
        Account account = accountRepository.findById(accId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Balance balance = account.getBalance();
        if (balance == null) {
            throw new RuntimeException("Balance not initialized");
        }

        balance.setAvailableBalance(balance.getAvailableBalance() + amount);
        balanceRepository.saveAndFlush(balance);

        redisService.save(BALANCE_KEY_PREFIX, accId, balance, BALANCE_CACHE_DURATION);
    }

    @Override
    public void deductMoney(Double amount, Long accId) {
        Account account = accountRepository.findById(accId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Balance balance = account.getBalance();
        if (balance == null) {
            throw new RuntimeException("Balance not initialized");
        }

        if (balance.getAvailableBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        balance.setAvailableBalance(balance.getAvailableBalance() - amount);
        balanceRepository.saveAndFlush(balance);

        redisService.save(BALANCE_KEY_PREFIX, accId, balance, BALANCE_CACHE_DURATION);
    }



}
