package com.Proton.JavaSpring.service.serviceImpl;

import com.Proton.JavaSpring.dto.request.accountDTO.CreateAccountDTO;
import com.Proton.JavaSpring.dto.request.accountDTO.UpdateAccountDTO;
import com.Proton.JavaSpring.entity.Account;
import com.Proton.JavaSpring.entity.Balance;
import com.Proton.JavaSpring.entity.Card;
import com.Proton.JavaSpring.repository.AccountRepository;
import com.Proton.JavaSpring.repository.BalanceRepository;
import com.Proton.JavaSpring.repository.CardRepository;
import com.Proton.JavaSpring.service.AccountService;
import com.Proton.JavaSpring.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final BalanceRepository balanceRepository;
    private final RedisService<Account> redisService;

    private static final Duration ACCOUNT_CACHE_DURATION = Duration.ofMinutes(10);
    private static final String ACCOUNT_KEY_PREFIX = "account:";

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              CardRepository cardRepository,
                              BalanceRepository balanceRepository,
                              RedisService<Account> redisService) {
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.balanceRepository = balanceRepository;
        this.redisService = redisService;
    }

    @Override
    public Account createAccount(CreateAccountDTO account) {
        if (accountRepository.findByEmail(account.getEmail()) != null) {
            throw new RuntimeException("Account already exists");
        }
        Account acc = new Account();
        acc.setCustomerName(account.getCustomerName());
        acc.setEmail(account.getEmail());
        acc.setPhoneNumber(account.getPhoneNumber());

        Account saved = accountRepository.save(acc);

        redisService.save(ACCOUNT_KEY_PREFIX, saved.getAccountId(), saved, ACCOUNT_CACHE_DURATION);

        return saved;
    }

    @Override
    public Account updateAccount(UpdateAccountDTO req) {
        Account acc = accountRepository.findById(req.getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (req.getEmail() != null) {
            acc.setEmail(req.getEmail());
        }
        if (req.getPhoneNumber() != null) {
            acc.setPhoneNumber(req.getPhoneNumber());
        }

        Account updated = accountRepository.save(acc);

        redisService.save(ACCOUNT_KEY_PREFIX,updated.getAccountId(), updated, ACCOUNT_CACHE_DURATION);

        return updated;
    }

    @Override
    public void deleteAccount(Long accountId) {
        List<Card> cards = cardRepository.findByAccount_AccountId(accountId);
        Balance balance = balanceRepository.findByAccount_AccountId(accountId);

        if (!cards.isEmpty()) throw new RuntimeException("Cannot delete: linked cards exist");
        if (balance != null && (balance.getAvailableBalance() > 0 || balance.getHoldBalance() > 0)) {
            throw new RuntimeException("Cannot delete: balance not zero");
        }

        Account acc = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        accountRepository.deleteById(accountId);


        redisService.delete(ACCOUNT_KEY_PREFIX,accountId);
    }

    @Override
    public Account getAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Account cached = redisService.get(ACCOUNT_KEY_PREFIX, id, Account.class);
        if (cached != null) {
            return cached;
        }

        redisService.save(ACCOUNT_KEY_PREFIX,account.getAccountId(),account, ACCOUNT_CACHE_DURATION);
        return account;
    }
}
