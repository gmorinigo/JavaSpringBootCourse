package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.IAccountServices;
import com.mindhub.homebanking.services.IClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    IAccountServices accountServices;

    @Autowired
    IClientServices clientServices;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccountsList(){
        return accountServices.findAll();
    }

    @GetMapping("accounts/{accountId}")
    public AccountDTO getAccount(@PathVariable Long accountId){
        return accountServices.findById(accountId);
    }

    @GetMapping("accounts/byBalance/{balance}")
    public List<AccountDTO> getAccountsByBalanceGreaterThan(@PathVariable double balance){
        return accountServices.findByBalanceGreaterThan(balance);
    }

    @GetMapping("accounts/findbycreationdatelessthan/{date}")
    public List<AccountDTO> getAccountsByCreationDateLessThan(@PathVariable String date){
        return accountServices.findByCreationDateLessThan(date);
    }

    @GetMapping("accounts/bynumber/{number}")
    public AccountDTO getAccountsByNumber(@PathVariable String number){
        return accountServices.findByNumber(number);
    }

    @PostMapping("clients/current/accounts")
    public ResponseEntity<Object> addAccount (Authentication authentication){
        Client aClient = clientServices.findByEmail(authentication.getName());

        if (aClient ==  null)
            return new ResponseEntity<>("Client Not Found", HttpStatus.NOT_FOUND);

        if (aClient.getAccounts().size() > 2)
            return new ResponseEntity<>("Maximum number of accounts exceeded", HttpStatus.FORBIDDEN);

        accountServices.generateAccount(aClient);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "clients/current/accounts")
    public List<AccountDTO> getAccounts (Authentication authentication){
        Client clientAuthenticated = clientServices.findByEmail(authentication.getName());

        /*if (clientAuthenticated ==  null)
            return new ResponseEntity<>("Client Not Found", HttpStatus.NOT_FOUND);*/

        List<AccountDTO> accountsOfClientAuthenticated = accountServices.findByClientDTO(clientAuthenticated);
        return accountsOfClientAuthenticated;
        //return new ResponseEntity<>(accountsOfClientAuthenticated, HttpStatus.OK);
    }
}
