package com.laptevn.bank.service;

import com.laptevn.bank.entity.Account;
import com.laptevn.bank.entity.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Retrieves bank information
 */
@Service
public class BankService {
    private final AccountService accountService;
    private final AccountNumberGenerator accountNumberGenerator;

    @Autowired
    public BankService(AccountService accountService, AccountNumberGenerator accountNumberGenerator) {
        this.accountService = accountService;
        this.accountNumberGenerator = accountNumberGenerator;
    }

    public Optional<Bank> getBank(int id, String authorizationHeader) {
        Optional<Account> account = accountService.getAccount(id, authorizationHeader);
        if (!account.isPresent()) {
            return Optional.empty();
        }

        Bank bank = new Bank()
                .setId(account.get().getId())
                .setFirstName(account.get().getFirstName())
                .setLastName(account.get().getLastName())
                .setIban(accountNumberGenerator.generate(account.get().getId()));
        return Optional.of(bank);
    }
}