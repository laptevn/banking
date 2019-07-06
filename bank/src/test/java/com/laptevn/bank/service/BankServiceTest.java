package com.laptevn.bank.service;

import com.laptevn.bank.entity.Bank;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BankServiceTest {
    @Test
    public void withoutAccount() {
        AccountService accountService = EasyMock.mock(AccountService.class);
        EasyMock.expect(accountService.getAccount(EasyMock.anyInt(), EasyMock.anyString())).andReturn(Optional.empty());
        EasyMock.replay(accountService);

        BankService bankService = new BankService(accountService, new AccountNumberGenerator());
        assertFalse(bankService.getBank(1, "").isPresent());
    }

    @Test
    public void withAccount() {
        AccountService accountService = EasyMock.mock(AccountService.class);
        EasyMock.expect(accountService.getAccount(EasyMock.anyInt(), EasyMock.anyString()))
                .andReturn(Optional.of(AccountCacheTest.account));
        EasyMock.replay(accountService);

        Optional<Bank> bank = new BankService(accountService, new AccountNumberGenerator()).getBank(1, "");
        assertEquals("Invalid id", AccountCacheTest.account.getId(), bank.get().getId());
        assertEquals("Invalid first name", AccountCacheTest.account.getFirstName(), bank.get().getFirstName());
        assertEquals("Invalid last name", AccountCacheTest.account.getLastName(), bank.get().getLastName());
        assertEquals("Invalid IBAN", "DE89370400440532013081", bank.get().getIban());
    }
}