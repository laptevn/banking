package com.laptevn.bank.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccountNumberGeneratorTest {
    @Test(expected = IllegalArgumentException.class)
    public void negativeAccountId() {
        new AccountNumberGenerator().generate(-1);
    }

    @Test
    public void oneSymbolAccountId() {
        String actualNumber = new AccountNumberGenerator().generate(1);
        assertEquals("DE89370400440532013081", actualNumber);
    }

    @Test
    public void maxSymbolsAccountId() {
        String actualNumber = new AccountNumberGenerator().generate(Integer.MAX_VALUE);
        assertEquals("DE89370400442147483647", actualNumber);
    }
}