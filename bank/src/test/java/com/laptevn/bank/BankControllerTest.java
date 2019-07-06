package com.laptevn.bank;

import com.laptevn.bank.entity.Bank;
import com.laptevn.bank.service.BankService;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class BankControllerTest {
    @Test
    public void withBank() {
        BankService bankService = EasyMock.mock(BankService.class);
        Bank expectedBank = new Bank();
        EasyMock.expect(bankService.getBank(EasyMock.anyInt(), EasyMock.anyString())).andReturn(Optional.of(expectedBank));
        EasyMock.replay(bankService);

        ResponseEntity<Bank> response = new BankController(bankService).getBank(1, "");
        assertEquals("Invalid status code", HttpStatus.OK, response.getStatusCode());
        assertSame("Invalid bank", expectedBank, response.getBody());
    }

    @Test
    public void withoutBank() {
        BankService bankService = EasyMock.mock(BankService.class);
        EasyMock.expect(bankService.getBank(EasyMock.anyInt(), EasyMock.anyString())).andReturn(Optional.empty());
        EasyMock.replay(bankService);

        ResponseEntity<Bank> response = new BankController(bankService).getBank(1, "");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}