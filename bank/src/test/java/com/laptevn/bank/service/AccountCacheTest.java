package com.laptevn.bank.service;

import com.laptevn.bank.entity.Account;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

public class AccountCacheTest {
    static final Account account = new Account()
            .setId(1)
            .setFirstName("John")
            .setLastName("Smith");

    @Test
    public void getFromEmptyCache() {
        assertFalse(new AccountCache().get(1).isPresent());
    }

    @Test
    public void putAndRemoveEntry() {
        AccountCache cache = new AccountCache();
        cache.update(account.getId(), Optional.of(account));

        assertSame("Account wasn't added", account, cache.get(account.getId()).get());
        assertFalse("Non existing account was retrieved", cache.get(2).isPresent());

        cache.update(account.getId(), Optional.empty());
        assertFalse("Account wasn't removed", cache.get(account.getId()).isPresent());
    }

    @Test
    public void updateEntry() {
        AccountCache cache = new AccountCache();
        cache.update(account.getId(), Optional.of(account));

        Account accountUpdated = new Account()
                .setId(account.getId())
                .setFirstName("Nikolai")
                .setLastName("Laptev");
        cache.update(account.getId(), Optional.of(accountUpdated));
        assertSame("Account wasn't updated", accountUpdated, cache.get(accountUpdated.getId()).get());
    }
}