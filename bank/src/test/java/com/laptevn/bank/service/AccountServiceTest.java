package com.laptevn.bank.service;

import com.laptevn.bank.entity.Account;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertSame;

public class AccountServiceTest {
    @Test
    public void updateCache() {
        AccountClient accountClient = EasyMock.mock(AccountClient.class);
        Optional<Account> expectedAccount = Optional.of(new Account());
        EasyMock.expect(accountClient.getAccount(EasyMock.anyInt(), EasyMock.anyString())).andReturn(expectedAccount);
        EasyMock.replay(accountClient);

        AccountCache accountCache = new AccountCache();
        AccountService accountService = new AccountService(accountClient, accountCache);
        int id = 1;
        Optional<Account> actualAccount = accountService.getAccount(id, "");
        assertSame("Account service doesn't return an account", expectedAccount, actualAccount);
        assertSame("Cache doesn't contain added account", expectedAccount.get(), accountCache.get(id).get());
        assertSame(
                "Fallback method for account service doesn't use cache",
                expectedAccount.get(),
                accountService.getAccountFromCache(id, "").get());
    }
}