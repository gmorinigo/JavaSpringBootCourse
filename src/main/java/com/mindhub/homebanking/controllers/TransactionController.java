package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.IAccountServices;
import com.mindhub.homebanking.services.IClientServices;
import com.mindhub.homebanking.services.ITransactionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    ITransactionServices transactionServices;

    @Autowired
    IClientServices clientServices;

    @Autowired
    IAccountServices accountServices;

    @GetMapping("/transactions/betweenDates/{dateFrom}/{dateTo}")
    public List<TransactionDTO> getTransactionsBetweenDates(@PathVariable String dateFrom, @PathVariable String dateTo){
        return transactionServices.findByDateBetween(dateFrom,dateTo);
    }

    @GetMapping("/transactions/byamountbetween/{amountFrom}/{amountTo}")
    public List<TransactionDTO> getTransactionsBetweenAmounts(@PathVariable double amountFrom,@PathVariable double amountTo){
        return transactionServices.findByAmountGreaterThanAndAmountLessThan(amountFrom,amountTo);
    }

    @GetMapping("/transactions/bytype/{type}")
    public List<TransactionDTO> getTransactionsByType(@PathVariable TransactionType type){
        // Cualquier valor diferente a los de TransactionType no hace que funcione el endpoint (En teoria ok)
        return transactionServices.findByType(type);
    }

    @Transactional
    @PostMapping(path = "/transactions")
    public ResponseEntity<Object> makeTransaction (@RequestParam double amount, @RequestParam String description,
                                                   @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
                                                   Authentication authentication){
        if (amount == 0 || description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("Origin and Destination Accounts must be different", HttpStatus.FORBIDDEN);
        }

        Client clientAutenthicated = clientServices.findByEmail(authentication.getName());
        if (clientAutenthicated ==  null)
            return new ResponseEntity<>("Client Not Found", HttpStatus.NOT_FOUND);

        Account originAccount = accountServices.findByAccountByNumber(fromAccountNumber);
        if (originAccount == null)
            return new ResponseEntity<>("Origin Account Not Found", HttpStatus.NOT_FOUND);

        if (!originAccount.getClient().equals(clientAutenthicated))
            return new ResponseEntity<>("Origin Account is not of the client Authenticated", HttpStatus.FORBIDDEN);

        Account destinationAccount = accountServices.findByAccountByNumber(toAccountNumber);
        if (destinationAccount == null)
            return new ResponseEntity<>("Destination Account Not Found", HttpStatus.NOT_FOUND);

        if (originAccount.getBalance() < amount)
            return new ResponseEntity<>("Insufficient Balance", HttpStatus.FORBIDDEN);

        transactionServices.makeTransaction(amount, description, originAccount, destinationAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
