package com.laptevn.bank.service;

import com.laptevn.bank.entity.Account;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Retrieves account information
 */
@Service
class AccountService {
    private final static Logger logger = LoggerFactory.getLogger(AccountService.class);
    private final AccountClient accountClient;
    private final AccountCache accountCache;

    @Autowired
    public AccountService(AccountClient accountClient, AccountCache accountCache) {
        this.accountClient = accountClient;
        this.accountCache = accountCache;
    }

    @HystrixCommand(fallbackMethod = "getAccountFromCache")
    public Optional<Account> getAccount(int id, String authorizationHeader) {
        Optional<Account> account = accountClient.getAccount(id, authorizationHeader);
        accountCache.update(id, account);
        return account;
    }

    public Optional<Account> getAccountFromCache(int id, String authorizationHeader) {
        logger.info("Account service is not available. Using local cache instead.");
        return accountCache.get(id);
    }
}