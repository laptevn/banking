package com.laptevn.bank.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Component;

/**
 * Generates IBAN for bank account
 */
@Component
@ThreadSafe
class AccountNumberGenerator {
    private static final String BASE_NUMBER = "DE89370400440532013087";

    public String generate(int accountId) {
        if (accountId < 0) {
            throw new IllegalArgumentException("Account id cannot be negative");
        }

        String accountIdText = Integer.toString(accountId);
        return BASE_NUMBER.substring(0, BASE_NUMBER.length() - accountIdText.length()) + accountIdText;
    }
}