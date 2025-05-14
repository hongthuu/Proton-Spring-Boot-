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
    private final RedisService redisService;

    private static final Duration ACCOUNT_CACHE_DURATION = Duration.ofHours(1); // TTL cho Redis

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              CardRepository cardRepository,
                              BalanceRepository balanceRepository,
                              RedisService redisService) {
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

        redisService.saveAccount(saved, ACCOUNT_CACHE_DURATION);

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

        // Cập nhật lại cache
        redisService.saveAccount(updated, ACCOUNT_CACHE_DURATION);

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

        // Xóa cache
        redisService.deleteAccount(accountId);
    }

    @Override
    public Account getAccount(Long id) {
        // Kiểm tra trong Redis
        Account cached = redisService.getAccount(id);
        if (cached != null) {
            return cached;
        }

        // Nếu không có, lấy từ DB và lưu vào cache
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        redisService.saveAccount(account, ACCOUNT_CACHE_DURATION);
        return account;
    }
}
