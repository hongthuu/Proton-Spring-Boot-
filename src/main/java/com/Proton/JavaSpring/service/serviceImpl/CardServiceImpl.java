package com.Proton.JavaSpring.service.serviceImpl;

import com.Proton.JavaSpring.dto.request.cardDTO.CreateCardDTO;
import com.Proton.JavaSpring.entity.Account;
import com.Proton.JavaSpring.entity.Card;
import com.Proton.JavaSpring.entity.Transaction;
import com.Proton.JavaSpring.repository.AccountRepository;
import com.Proton.JavaSpring.repository.CardRepository;
import com.Proton.JavaSpring.repository.TransactionRepository;
import com.Proton.JavaSpring.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @Override
    public List<Card> getListCard(Long id) {
        if (accountRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Account not exists");
        }
        return cardRepository.findByAccount_AccountId(id);
    }

    @Override
    public Card addCard(CreateCardDTO cardDTO) {
        Account account = accountRepository.findById(cardDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Card card = new Card();
        card.setAccount(account);
        card.setCardType(cardDTO.getCardType());
        card.setStatus(cardDTO.getStatus());
        card.setExpiryDate(cardDTO.getExpiryDate());
        return cardRepository.save(card);
    }

    @Override
    public void deleteCard(Long cardId) {
        if (cardRepository.findById(cardId).isEmpty()) {
            throw new RuntimeException("Card not exists");
        }
        List<Transaction> transactions = transactionRepository.findByCard_CardIdAndStatus(cardId, "PENDING");
        if (!transactions.isEmpty()) {
            throw new RuntimeException("Transaction is pending");
        }
        cardRepository.deleteById(cardId);
    }
}
