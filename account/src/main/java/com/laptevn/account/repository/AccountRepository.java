package com.laptevn.account.repository;

import com.laptevn.account.Account;

import java.util.Collection;
import java.util.Optional;

/**
 * Handles account data stored in repository
 */
public interface AccountRepository {
    Collection<Account> getAll();
    void add(Account account) throws RepositoryException;
    Optional<Account> get(int id);
}