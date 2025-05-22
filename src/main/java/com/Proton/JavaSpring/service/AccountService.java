package com.Proton.JavaSpring.service;

import com.Proton.JavaSpring.dto.request.accountDTO.AccountDTO;
import com.Proton.JavaSpring.dto.request.accountDTO.CreateAccountDTO;
import com.Proton.JavaSpring.dto.request.accountDTO.UpdateAccountDTO;
import com.Proton.JavaSpring.entity.Account;

public interface AccountService {
    Account createAccount(CreateAccountDTO account);

    Account updateAccount(UpdateAccountDTO updateAccountDTO);

    void deleteAccount(Long id);

    AccountDTO getAccount(Long id);
}
