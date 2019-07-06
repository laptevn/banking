package com.laptevn.account.repository;

import com.laptevn.account.Account;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class InMemoryAccountRepositoryTest {
    private final static int ACCOUNT_ID = 1;
    public final static Account account = new Account()
            .setId(ACCOUNT_ID)
            .setFirstName("John")
            .setLastName("Smith");

    @Test
    public void emptyRepository() {
        InMemoryAccountRepository repository = new InMemoryAccountRepository();
        assertTrue("Repository should be empty", repository.getAll().isEmpty());
        assertFalse("No account should be found in empty repository", repository.get(ACCOUNT_ID).isPresent());
    }

    @Test(expected = RepositoryException.class)
    public void addDuplicate() throws RepositoryException {
        InMemoryAccountRepository repository = new InMemoryAccountRepository();
        repository.add(account);

        Account account2 = new Account()
                .setId(ACCOUNT_ID)
                .setFirstName("Adam")
                .setLastName("Black");
        repository.add(account2);
    }

    @Test
    public void addAccount() throws RepositoryException {
        InMemoryAccountRepository repository = new InMemoryAccountRepository();
        assertTrue("Repository should be empty", repository.getAll().isEmpty());

        repository.add(account);
        assertEquals("New account wasn't added", 1, repository.getAll().size());
    }

    @Test
    public void getExistingAccount() throws RepositoryException {
        InMemoryAccountRepository repository = new InMemoryAccountRepository();
        repository.add(account);

        Optional<Account> searchingAccount = repository.get(account.getId());
        assertTrue("Added account wasn't found", searchingAccount.isPresent());
        assertSame("Found account is not same as added one", account, searchingAccount.get());
    }
}