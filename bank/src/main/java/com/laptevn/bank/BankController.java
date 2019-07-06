package com.laptevn.bank;

import com.laptevn.bank.entity.Bank;
import com.laptevn.bank.service.BankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * A facade for operations over bank information
 */
@RestController
public class BankController {
    private final static Logger logger = LoggerFactory.getLogger(BankController.class);

    private final BankService bankService;

    @Autowired
    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @RequestMapping(value = "/bank/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Bank> getBank(@PathVariable("id") int id, @RequestHeader("authorization") String authorizationHeader) {
        logger.info("Retrieving bank information with {} account id", id);

        Optional<Bank> bank = bankService.getBank(id, authorizationHeader);
        if (!bank.isPresent()) {
            logger.info("No bank information found with {} id", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("Bank information with {} id was found", id);
        return new ResponseEntity<>(bank.get(), HttpStatus.OK);
    }
}