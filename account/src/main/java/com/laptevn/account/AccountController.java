package com.laptevn.account;

import com.laptevn.account.repository.AccountRepository;
import com.laptevn.account.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;

/**
 * A facade for operations over accounts
 */
@RestController
public class AccountController {
    private static final String ERROR_INVALID_ID = "Id doesn't correspond to account content";

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final AccountRepository accountRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @RequestMapping(value = "/accounts/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Account> getAccounts() {
        Collection<Account> accounts = accountRepository.getAll();
        logger.info("{} accounts retrieved", accounts.size());
        return accounts;
    }

    @RequestMapping(value = "/account/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createAccount(@RequestBody Account account, @PathVariable("id") int id) {
        logger.info("Creating an account with {} id", id);

        if (id != account.getId()) {
            logger.info(ERROR_INVALID_ID);
            return new ResponseEntity<>(ERROR_INVALID_ID, HttpStatus.BAD_REQUEST);
        }

        try {
            accountRepository.add(account);
        } catch (RepositoryException e) {
            logger.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        logger.info("New account is created");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/account/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getAccount(@PathVariable("id") int id) {
        Optional<Account> account = accountRepository.get(id);
        if (!account.isPresent()) {
            logger.info("No account found with {} id", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("An account with {} id was found", id);
        return new ResponseEntity<>(account.get(), HttpStatus.OK);
    }
}