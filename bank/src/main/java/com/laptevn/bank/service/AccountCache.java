package com.laptevn.bank.service;

import com.laptevn.bank.entity.Account;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Stores cached accounts
 */
@Component
@ThreadSafe
class AccountCache {
    private final Map<Integer, Account> idToAccountIndex = new HashMap<>();

    public synchronized Optional<Account> get(int id) {
        return idToAccountIndex.containsKey(id)
                ? Optional.of(idToAccountIndex.get(id))
                : Optional.empty();
    }

    public synchronized void update(int id, Optional<Account> account) {
        if (account.isPresent()) {
            idToAccountIndex.put(id, account.get());
        } else  {
            idToAccountIndex.remove(id);
        }
    }
}