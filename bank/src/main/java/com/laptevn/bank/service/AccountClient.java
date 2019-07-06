package com.laptevn.bank.service;

import com.laptevn.bank.entity.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

/**
 * Client for account service
 */
@FeignClient(value = "account", decode404 = true)
interface AccountClient {
    @RequestMapping(value = "/account/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    Optional<Account> getAccount(@PathVariable("id") int id, @RequestHeader("authorization") String authorizationHeader);
}