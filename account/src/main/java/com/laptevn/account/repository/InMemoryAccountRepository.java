package com.laptevn.account.repository;

import com.laptevn.account.Account;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Account repository based on in memory storage
 */
@Component
@ThreadSafe
public class InMemoryAccountRepository implements AccountRepository {
    private final Map<Integer, Account> idToAccountIndex = new HashMap<>();

    @Override
    public synchronized Collection<Account> getAll() {
        return idToAccountIndex.values();
    }

    @Override
    public synchronized void add(Account account) throws RepositoryException {
        if (idToAccountIndex.containsKey(account.getId())) {
            throw new RepositoryException("An account with this id already exists");
        }

        idToAccountIndex.put(account.getId(), account);
    }

    @Override
    public synchronized Optional<Account> get(int id) {
        return idToAccountIndex.containsKey(id)
                ? Optional.of(idToAccountIndex.get(id))
                : Optional.empty();
    }
}